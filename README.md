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

O projeto possui um `Jenkinsfile` na raiz com uma pipeline Jenkins local para build, testes, package, verificacao de dependencias, validacao documental e deploy local do artefato.

Etapas executadas pela pipeline:

- `Build`: compila o projeto com Maven Wrapper.
- `Testes Automatizados`: executa testes unitarios e publica relatorio JUnit.
- `Package`: gera o JAR da aplicacao e valida o `Main-Class` no manifesto.
- `OWASP Dependency Check`: executa analise de dependencias quando possivel. Sem NVD API Key, essa etapa pode demorar bastante; por isso ela e tratada como analise nao bloqueante no Jenkins local.
- `Validacao do README`: confere evidencias minimas de Jenkins, Uso de IA e prompts.
- `Validacao das Historias e Rastreabilidade`: executa `scripts/validate_docs.sh`.
- `Deploy Local`: publica o pacote em `target/deploy/game-hub` e arquiva o deploy no Jenkins.

Comandos equivalentes para validar localmente no Windows:

```powershell
.\mvnw.cmd -B clean test
.\mvnw.cmd -B package -DskipTests
```

Comandos equivalentes para validar localmente no Linux:

```bash
./mvnw -B clean test
./mvnw -B package -DskipTests
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

### Acessar build, artefatos e deploy local

Com o Jenkins rodando localmente, acesse:

```text
http://localhost:8080/job/grupo2-build/
```

Para ver o log da build, abra a build desejada e clique em `Console Output`.

Para ver os artefatos compilados, abra a build desejada e clique em `Artifacts`.

Artefatos esperados:

- `target/grupo2projeto-1.0-SNAPSHOT.jar`
- `target/deploy/game-hub/grupo2projeto-1.0-SNAPSHOT.jar`
- `target/deploy/game-hub/README_DEPLOY.txt`
- `target/deploy/game-hub/SHA256SUMS.txt`

O diretorio `target/deploy/game-hub` representa o deploy local da aplicacao desktop gerado pela pipeline.

### Parar e iniciar novamente

Para parar o Jenkins:

```powershell
docker stop grupo2-jenkins
```

Para iniciar novamente:

```powershell
docker start grupo2-jenkins
```

Observacao: os testes JavaFX de controller/tela podem exigir bibliotecas graficas adicionais quando o Jenkins roda em container Linux. Por isso, no Jenkins Linux a pipeline executa os testes de dominio e logica que rodam sem interface grafica. Em ambiente Windows local, `.\mvnw.cmd -B clean test` executa a suite completa.

## 🤖 Uso de Inteligência Artificial
Em conformidade com as diretrizes do projeto, declaramos o uso consciente e transparente de ferramentas de IA durante o desenvolvimento.
### Modelos Utilizados

**Gemini / ChatGPT / Claude:** Utilizados para auxiliar na arquitetura do projeto, implementação de funcionalidades, refatoração de código, elaboração de testes unitários, documentação e criação de scripts de automação.

### Dinâmica de Uso

A IA foi utilizada de forma colaborativa durante o desenvolvimento do projeto, auxiliando na definição da arquitetura, geração de sugestões de implementação, criação de testes unitários, documentação e desenvolvimento de scripts auxiliares para a pipeline de CI/CD.

**O que não foi feito por IA:** Toda a lógica de negócio central, as decisões arquiteturais finais, a integração entre módulos e a validação das funcionalidades foram realizadas manualmente pela equipe.

#### Prompt 1
- **Objetivo:** Criação da estrutura básica do projeto.
- **Prompt utilizado:**
  ```text
  Gemini, preciso iniciar um projeto Java utilizando JavaFX para uma plataforma de jogos. Gostaria que você sugerisse uma estrutura de pastas organizada, seguindo boas práticas de desenvolvimento, separando telas, controladores, modelos e recursos da aplicação.
  ```
- **Conclusão:** A IA auxiliou na definição da estrutura inicial do projeto, sugerindo uma organização clara para os diretórios e arquivos. A proposta serviu como base para o desenvolvimento, facilitando a manutenção e a expansão futura da aplicação.

#### Prompt 2
- **Objetivo:** Criação e reestruturação do menu principal.
- **Prompt utilizado:**
  ```text
  Gemini, temos um menu principal funcional, mas gostaríamos de melhorar sua organização visual e estrutural. Poderia sugerir uma nova implementação utilizando componentes JavaFX e aplicar melhorias na navegação entre telas?
  ```
- **Conclusão:** A IA forneceu sugestões para reorganização dos elementos visuais do menu e melhorias na navegação da aplicação. As recomendações foram adaptadas pela equipe para atender aos requisitos específicos do projeto.

#### Prompt 3
- **Objetivo:** Implementação das lógicas dos jogos.
- **Prompt utilizado:**
  ```text
  ChatGPT, preciso implementar a lógica de um jogo em Java. Considere regras de vitória, derrota, empate e validação das jogadas. Poderia sugerir uma estrutura orientada a objetos que facilite testes e manutenção?
  ```
- **Conclusão:** A IA auxiliou na modelagem das regras de negócio dos jogos, sugerindo classes, métodos e fluxos de execução. As respostas serviram como referência para a implementação final realizada pela equipe.

#### Prompt 4
- **Objetivo:** Refatoração da lógica base do sistema.
- **Prompt utilizado:**
  ```text
  Claude, analise este trecho de código e sugira melhorias de refatoração seguindo os princípios SOLID e boas práticas de programação orientada a objetos. O objetivo é reduzir duplicação de código e melhorar a legibilidade.
  ```
- **Conclusão:** A IA identificou pontos de melhoria na arquitetura e sugeriu alterações que contribuíram para tornar o código mais organizado, reutilizável e de fácil manutenção. As mudanças propostas foram avaliadas e aplicadas conforme a necessidade do projeto.

#### Prompt 5
- **Objetivo:** Auxílio na criação e compreensão das histórias de usuário.
- **Prompt utilizado:**
  ```text
  ChatGPT, analise os requisitos deste projeto e me ajude a criar histórias de usuário seguindo o padrão: "Como [tipo de usuário], quero [objetivo], para que [benefício]". Além disso, sugira critérios de aceitação para cada história.
  ```
- **Conclusão:** A IA auxiliou na elaboração das histórias de usuário e dos critérios de aceitação, ajudando a equipe a compreender melhor os requisitos do sistema e a manter a documentação alinhada com os objetivos do projeto.

#### Prompt 6
- **Objetivo:** Criação de testes unitários para as regras dos jogos.
- **Prompt utilizado:**
  ```text
  ChatGPT, preciso criar testes unitários para as regras de negócio do meu jogo em Java utilizando JUnit 5. Poderia sugerir cenários de teste cobrindo casos de sucesso, falha e situações de borda?
  ```
- **Conclusão:** A IA auxiliou na identificação dos principais cenários de teste, servindo como apoio para aumentar a cobertura dos testes e validar corretamente as regras implementadas.

#### Prompt 7
- **Objetivo:** Implementação da navegação entre telas.
- **Prompt utilizado:**
  ```text
  Gemini, estou desenvolvendo uma aplicação JavaFX com múltiplas telas. Qual seria a melhor forma de realizar a troca de cenas mantendo o código organizado e evitando duplicação?
  ```
- **Conclusão:** A IA apresentou diferentes abordagens para gerenciamento de telas, auxiliando na definição da arquitetura utilizada para a navegação da aplicação.

#### Prompt 8
- **Objetivo:** Criação da pipeline de integração contínua.
- **Prompt utilizado:**
  ```text
  Claude, preciso criar uma pipeline de CI para um projeto Java Maven. A pipeline deve compilar o projeto, executar validações e apresentar logs claros em caso de falha. Pode me fornecer um exemplo?
  ```
- **Conclusão:** A IA forneceu uma estrutura inicial para a pipeline, que foi adaptada pela equipe e utilizada como base para a configuração do processo de integração contínua.

#### Prompt 9
- **Objetivo:** Documentação do projeto.
- **Prompt utilizado:**
  ```text
  ChatGPT, analise a estrutura do meu projeto e sugira uma documentação README contendo instruções de instalação, execução, funcionalidades e organização do sistema.
  ```
- **Conclusão:** A IA auxiliou na elaboração da documentação inicial do projeto, servindo como referência para a criação e padronização das informações disponibilizadas aos usuários e desenvolvedores.

#### Prompt 10
- **Objetivo:** Resolução de erros e depuração de código.
- **Prompt utilizado:**
  ```text
  Gemini, estou recebendo uma exceção durante a execução desta funcionalidade. Analise o código e a mensagem de erro, identifique possíveis causas e sugira formas de corrigir o problema.
  ```
- **Conclusão:** A IA ajudou no processo de depuração ao apontar possíveis origens do erro e sugerir estratégias de correção, reduzindo o tempo necessário para identificar a falha e implementar a solução.

### Considerações Finais

As ferramentas de Inteligência Artificial foram utilizadas como apoio ao desenvolvimento, funcionando como assistentes para pesquisa, documentação e sugestões técnicas. Todas as respostas geradas foram analisadas, adaptadas e validadas pela equipe antes de serem incorporadas ao projeto.