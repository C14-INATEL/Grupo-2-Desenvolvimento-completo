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
.\mvnw.cmd "-Dtest=mocks.*" test
```

