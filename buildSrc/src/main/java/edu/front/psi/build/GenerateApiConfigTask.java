package edu.front.psi.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;

public abstract class GenerateApiConfigTask extends DefaultTask {
    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract ConfigurableFileCollection getConfigFiles();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @TaskAction
    public void generate() throws IOException {
        List<File> files = new ArrayList<>(getConfigFiles().getFiles());
        File envFile = files.stream()
                .filter(file -> file.getName().equals(".env"))
                .findFirst()
                .orElse(null);
        File exampleFile = files.stream()
                .filter(file -> file.getName().equals(".env.example"))
                .findFirst()
                .orElse(null);
        File configFile = envFile != null && envFile.isFile() ? envFile : exampleFile;

        if (configFile == null || !configFile.isFile()) {
            throw new GradleException("Configure API_BASE_URL em .env ou .env.example");
        }

        String baseUrl = validateBaseUrl(readBaseUrl(configFile));

        File outputFile = getOutputDirectory().file(
                "edu/front/psi/clin/api/ApiConfig.kt"
        ).get().getAsFile();
        File parent = outputFile.getParentFile();
        if (!parent.mkdirs() && !parent.isDirectory()) {
            throw new GradleException("Não foi possível criar o diretório de ApiConfig");
        }

        String escapedBaseUrl = baseUrl
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("$", "\\$");
        String source = "package edu.front.psi.clin.api\n\n"
                + "object ApiConfig {\n"
                + "    const val BASE_URL = \"" + escapedBaseUrl + "\"\n"
                + "}\n";
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(outputFile), StandardCharsets.UTF_8)) {
            writer.write(source);
        }
    }

    private String readBaseUrl(File configFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String value = line.trim();
                if (value.isEmpty() || value.startsWith("#")) {
                    continue;
                }

                int separator = value.indexOf('=');
                if (separator < 0 || !value.substring(0, separator).trim().equals("API_BASE_URL")) {
                    continue;
                }

                return value.substring(separator + 1).trim()
                        .replaceAll("^\"|\"$", "")
                        .replaceAll("^'|'$", "");
            }
        }

        throw new GradleException("API_BASE_URL não foi definido em " + configFile.getName());
    }

    private String validateBaseUrl(String baseUrl) {
        String normalizedUrl = baseUrl.replaceAll("/+$", "");
        URI uri;
        try {
            uri = URI.create(normalizedUrl);
        } catch (IllegalArgumentException exception) {
            throw new GradleException("API_BASE_URL deve ser uma URL válida.", exception);
        }

        String scheme = uri.getScheme();
        boolean isHttp = "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
        if (!isHttp || uri.getHost() == null || uri.getHost().isBlank()) {
            throw new GradleException(
                    "API_BASE_URL deve ser uma URL HTTP ou HTTPS válida e conter um host.");
        }
        return normalizedUrl;
    }
}