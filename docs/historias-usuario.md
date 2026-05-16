# Historias de Usuario

Este documento registra as historias de usuario do projeto e a rastreabilidade entre backlog, codigo, revisao e testes automatizados.

## US-01 - Acessar e usar o menu principal

**Como** jogador,  
**eu quero** acessar um menu principal com identificacao do Game Hub, perfil do usuario e lista de jogos,  
**para que** eu possa reconhecer a plataforma, personalizar meu nickname e iniciar um jogo disponivel.

**Prioridade:** Alta  
**Status:** Entregue  
**Issue/Card:** SCRUM-0  
**PR:** #6  
**Commit:** `9c72798`  
**Teste:** `MenuTest`

### Criterios de aceitacao

**Cenario 1 - Exibir titulo do menu principal**  
Dado que o jogador abre a aplicacao  
Quando a tela de menu principal e carregada  
Entao o sistema deve exibir o titulo `GAME HUB`.

**Cenario 2 - Atualizar nickname do jogador**  
Dado que o jogador esta no menu principal  
Quando ele informa um novo nickname valido  
Entao o sistema deve atualizar a mensagem de boas-vindas com o novo nome.

**Cenario 3 - Iniciar jogo selecionado pela lista**  
Dado que o jogador esta no menu principal  
E seleciona o jogo `Pedra, Papel e Tesoura`  
Quando ele solicita o inicio do jogo  
Entao o sistema deve direcionar o jogador para a tela correspondente ao jogo selecionado.

## US-02 - Vitoria do Computador nas Rodadas

**Como** jogador,  
**eu quero** que o sistema valide corretamente as jogadas do computador,  
**para que** as regras tradicionais do jogo sejam aplicadas de forma justa quando eu perder.

**Prioridade:** Alta  
**Status:** Entregue  
**Issue/Card:** Criar historias de usuario com rastreabilidade  
**PR:** #10  
**Commit:** `c4ac25bd8124f98e5409406326dc8da2fe5382a2`  
**Teste:** `computadorDeveVencerComPapelContraPedra`, `computadorDeveVencerComTesouraContraPapel`, `computadorDeveVencerComPedraContraTesoura`

### Criterios de aceitacao

**Cenario 1 - Computador vence com Papel contra Pedra** Dado que o jogador escolheu `pedra`  
E o computador escolheu `papel`  
Quando o metodo `model.getResult("pedra", "papel")` for executado  
Entao o sistema deve retornar que o vencedor e o `Computador`.

**Cenario 2 - Computador vence com Tesoura contra Papel** Dado que o jogador escolheu `papel`  
E o computador escolheu `tesoura`  
Quando o metodo `model.getResult("papel", "tesoura")` for executado  
Entao o sistema deve retornar que o vencedor e o `Computador`.

**Cenario 3 - Computador vence com Pedra contra Tesoura** Dado que o jogador escolheu `tesoura`  
E o computador escolheu `pedra`  
Quando o metodo `model.getResult("tesoura", "pedra")` for executado  
Entao o sistema deve retornar que o vencedor e o `Computador`.

## US-03 - Jogar Campo Minado e reiniciar a partida

**Como** jogador,  
**eu quero** clicar nas casas do campo minado e reiniciar a partida quando perder,  
**para que** eu possa jogar novas rodadas e continuar utilizando o jogo.

**Prioridade:** Alta  
**Status:** Entregue  
**Issue/Card:** SCRUM-0  
**PR:** #16  
**Commit:** `4f780255556348a01f8624ee1eee91bb69918c66`  
**Teste:** `deveMostrarFimDeJogoAoClicarEmMina`, `deveCriarNovoTabuleiroAoClicarEmNovoJogo`, `deveVoltarParaMenuAoClicarEmVoltar`

### Criterios de aceitacao

**Cenario 1 - Jogador perde ao clicar em uma mina**  
Dado que o jogador iniciou uma partida de Campo Minado  
E clicou em uma celula contendo mina  
Quando o sistema processa a jogada  
Entao o jogo deve exibir a mensagem de fim de jogo  
E o botao de reinicio deve ficar visivel.

**Cenario 2 - Reiniciar partida**  
Dado que o jogador esta em uma partida de Campo Minado  
Quando ele clicar em `Novo Jogo`  
Entao o sistema deve criar um novo tabuleiro  
E reiniciar o estado da partida.

**Cenario 3 - Retornar ao menu principal**  
Dado que o jogador esta na tela do Campo Minado  
Quando ele clicar no botao voltar  
Entao o sistema deve retornar ao menu principal.