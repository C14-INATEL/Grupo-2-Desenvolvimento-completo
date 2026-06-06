# Grupo-2-Desenvolvimento-completo

Desenvolvimento da plataforma de jogos by grupo 2

## Requisitos

- Java 17 ou superior instalado
- Terminal aberto na pasta raiz do projeto

## Como rodar o projeto

Este projeto usa Maven Wrapper, entao nao precisa ter o `mvn` instalado globalmente.

No Terminal, execute:

```powershell
.\mvnw.cmd javafx:run
```

## Funcionalidades

- Menu principal com listagem dos jogos disponiveis.
- Tela de detalhes para cada jogo, com nome, icone e descricao antes de iniciar a partida.
- Navegacao entre menu, tela de detalhes e telas dos jogos.
- Alteracao de nickname do jogador.
- Painel de configuracoes do perfil.
- Adicao e remocao de jogos na lista do menu.
- Jogo Pedra, Papel e Tesoura com escolha do jogador, jogada do computador, resultado da rodada e placar.
- Campo Minado com tabuleiro interativo, deteccao de minas, fim de jogo e reinicio de partida.
- Jogo da Velha com tabuleiro, controle de rodada e navegacao de volta ao menu.
- Logica de Batalha Naval implementada e coberta por testes, ainda sem tela propria integrada ao menu.

## Como usar a aplicacao

Ao abrir o projeto com `.\mvnw.cmd javafx:run`, a primeira tela exibida e o menu principal do Game Hub.

1. Selecione um jogo na lista do menu.
2. Clique em `Jogar` para abrir a tela de detalhes do jogo selecionado.
3. Na tela de detalhes, confira a descricao e clique em `Jogar` novamente para iniciar.
4. Use o botao de voltar nas telas dos jogos para retornar ao menu principal.
5. Para personalizar o perfil, abra o painel de configuracoes e altere o nickname.
6. Para testar a lista de jogos, use os botoes de adicionar ou remover jogo no menu.

Jogos disponiveis pela interface:

- Pedra, Papel e Tesoura
- Campo Minado
- Jogo da Velha

## Como rodar os testes unitários

Para garantir o bom funcionamento das lógicas e das regras de negócio do sistema (como jogos, vitória, campo minado etc.), execute o seguinte comando no terminal:

```powershell
.\mvnw.cmd clean test
```

## Como rodar os testes mock

Para rodar todos os testes que usam mock das lógicas e regras de negócio do sistema, execute o seguinte comando no terminal:

```powershell
.\mvnw.cmd "-Dtest=mocks.**" test
```

## Validar documentacao

Para validar a documentacao (historias e rastreabilidade), execute:

```bash
bash scripts/validate_docs.sh
```

No PowerShell, usando Git Bash:

```powershell
& "$env:PROGRAMFILES\Git\bin\bash.exe" -lc "./scripts/validate_docs.sh"
```

Se aparecer erro com $'\r', converta o arquivo para LF:

```bash
dos2unix scripts/validate_docs.sh
```

No PowerShell:

```powershell
$path = Resolve-Path 'scripts/validate_docs.sh'; $content = [System.IO.File]::ReadAllText($path); $content = $content -replace "`r`n", "`n"; [System.IO.File]::WriteAllText($path, $content)
```

## Como rodar o Jenkins localmente

O projeto possui um `Jenkinsfile` na raiz com uma pipeline simples. Nesta primeira versao, o Jenkins executa o build e valida a documentacao.

Build:

```powershell
.\mvnw.cmd -B clean compile
```

ou, quando estiver rodando em Linux:

```bash
./mvnw -B clean compile
```

Validacao de documentacao:

```bash
./scripts/validate_docs.sh
```

### Requisitos

- Docker Desktop instalado e aberto
- Acesso a internet para baixar a imagem do Jenkins e as dependencias Maven
- Porta `8080` livre na maquina

### Subir o Jenkins com Docker

No terminal, execute:

```powershell
docker pull jenkins/jenkins:lts-jdk17
docker run -d --name grupo2-jenkins -p 8080:8080 -p 50000:50000 -e JAVA_OPTS=-Djenkins.install.runSetupWizard=false -v grupo2_jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17
```

Instale os plugins basicos para Pipeline, Git e JUnit:

```powershell
docker exec grupo2-jenkins jenkins-plugin-cli --plugins workflow-aggregator git junit
docker restart grupo2-jenkins
```

Depois acesse:

```text
http://localhost:8080
```

Se o Jenkins pedir login, use o usuario `admin`. A senha inicial pode ser obtida com:

```powershell
docker exec grupo2-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

### Criar o job da pipeline

1. Clique em `New Item`.
2. Informe o nome `grupo2-build`.
3. Selecione `Pipeline`.
4. Em `Pipeline`, escolha `Pipeline script from SCM`.
5. Em `SCM`, selecione `Git`.
6. Informe a URL do repositorio:

```text
https://github.com/C14-INATEL/Grupo-2-Desenvolvimento-completo.git
```

7. Em `Branch Specifier`, use:

```text
*/main
```

8. Em `Script Path`, use:

```text
Jenkinsfile
```

9. Salve e clique em `Build Now`.

### Acessar build e artefatos

Com o Jenkins rodando localmente, acesse:

```text
http://localhost:8080/job/grupo2-build/
```

Para ver o log da build, abra a build desejada e clique em `Console Output`.

Para ver os artefatos compilados, abra a build desejada e clique em `Artifacts`.

### Parar e iniciar novamente

Para parar o Jenkins:

```powershell
docker stop grupo2-jenkins
```

Para iniciar novamente:

```powershell
docker start grupo2-jenkins
```

Observacao: os testes JavaFX podem exigir configuracao grafica adicional quando o Jenkins roda em container Linux. Por isso, a pipeline inicial valida apenas a compilacao do projeto.

## 🤖 Uso de Inteligência Artificial

[cite_start]Em conformidade com as diretrizes do projeto, declaramos o uso consciente e transparente de ferramentas de IA durante o desenvolvimento[cite: 89].

### Modelos Utilizados
* [cite_start]**Gemini / ChatGPT / Claude:** Utilizados para auxiliar na arquitetura do projeto, modelagem do banco de dados e criação de scripts de automação[cite: 93, 94].

### Dinâmica de Uso
* [cite_start]A IA foi utilizada de forma colaborativa para pair programming, geração de templates de testes unitários e desenvolvimento de scripts auxiliares para a pipeline de CI/CD.
* [cite_start]**O que não foi feito por IA:** Toda a lógica de negócio central, regras de escopo específicas e a integração final dos módulos foram desenvolvidas manualmente pelo grupo[cite: 97].

### Exemplos de Prompts Reais
1. *"Como estruturar uma pipeline declarativa no Jenkins que filtre comandos por sistema operacional?"* -> **Resultado:** Aceito e adaptado no `Jenkinsfile`.
2. *"Crie um script em Bash para validar a existência de arquivos específicos de documentação em um repositório."* -> **Resultado:** Aceito e refinado no script `validate_docs.sh`.
3. [cite_start]*"Analise o documento enviado, logo após isso, liste cada tópico fazendo um check-list para ser seguido no decorrer do processo."* [cite: 95]
