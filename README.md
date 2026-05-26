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

## Como rodar o Jenkins localmente

O projeto possui um `Jenkinsfile` na raiz com uma pipeline simples de build. Nesta primeira versao, o Jenkins executa:

```powershell
.\mvnw.cmd -B clean compile
```

ou, quando estiver rodando em Linux:

```bash
./mvnw -B clean compile
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

