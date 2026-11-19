# PsiClin

Aplicação multiplataforma para gerenciamento de uma clínica de psicologia, desenvolvida com Kotlin Multiplatform e Compose Multiplatform.

## Atualização de perfis

Ao editar um Funcionário, Psicólogo ou Recepcionista, o botão **Salvar** envia os campos alterados em uma única requisição `PUT` para a rota de atualização do perfil. Se uma nova senha for informada, ela segue no mesmo corpo da requisição; se o campo ficar vazio, a senha atual deve ser mantida. Não é feita uma chamada separada para alteração de senha.

O backend deve aceitar a senha opcional no corpo das rotas `PUT` de atualização e manter a senha existente quando esse campo não for enviado.

## Ícones dos builds

- Android usa os ícones adaptativos e as imagens por densidade em `androidApp/src/main/res/mipmap-*`. O ícone da Play Store está em `androidApp/store-assets/play_store_512.png`.
- Desktop usa `desktopApp/icons/PsiClin.ico` no Windows e `desktopApp/icons/PsiClin.png` no Linux. Ambos são derivados do ícone Web de 512 x 512 px.
- Web usa `webApp/src/webMain/resources/site.webmanifest`, favicons e ícone Apple Touch.

Os assets de Chrome Extension fornecidos não estão ligados a um build, pois este projeto não contém um módulo de extensão.
