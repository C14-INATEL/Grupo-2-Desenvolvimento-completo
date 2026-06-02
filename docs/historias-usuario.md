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

## US-04 - Exibir Resultado da Rodada

**Como** jogador,  
**eu quero** visualizar claramente o resultado de cada rodada apos fazer minha jogada,  
**para que** eu saiba imediatamente se venci, perdi ou empatei.

**Prioridade:** Alta  
**Status:** Entregue  
**Issue/Card:** SCRUM-0  
**PR:** #14  
**Commit:** `bd10be658801ddafb632fb8718b819fbee466400`  
**Teste:** `RockPaperScissorsViewControllerMockTest`

### Criterios de aceitacao

**Cenario 1 - Exibir vitoria do jogador**  
Dado que o jogador venceu a rodada (ex: `pedra` contra `tesoura`)  
Quando o sistema processar o resultado  
Entao deve exibir a mensagem `"Voce venceu a rodada."` no `resultLabel`.

**Cenario 2 - Atualizar placar do jogador**  
Dado que o jogador venceu a rodada  
Quando o resultado for processado  
Entao o placar do jogador deve ser incrementado em `1`  
E o metodo `updateScoreboard()` deve ser chamado.

**Cenario 3 - Atualizar quantidade de rodadas**  
Dado que uma rodada foi finalizada  
Quando o sistema atualizar o placar  
Entao a quantidade de rodadas jogadas deve ser incrementada em `1`.

## US-05 - Navegar para o Jogo pela Tela de Detalhes

**Como** jogador,  
**eu quero** clicar no botao "Jogar" na tela de detalhes de um jogo,  
**para que** eu seja direcionado corretamente para a tela do jogo selecionado.

**Prioridade:** Alta  
**Status:** Entregue  
**Issue/Card:** SCRUM-0  
**PR:** #17  
**Commit:** `d535d882b901eecb1c6505ed8acda44c205d15ed`  
**Teste:** `GameDetailControllerMockTest`

### Criterios de aceitacao

**Cenario 1 - Navegar para o Campo Minado ao clicar em Jogar**  
Dado que o jogador esta na tela de detalhes do jogo `Campo Minado`  
Quando ele clicar no botao `Jogar`  
Entao o sistema deve navegar para a tela do Campo Minado  
E nenhuma outra tela de jogo deve ser aberta.

**Cenario 2 - Nao navegar quando nenhum jogo estiver configurado**  
Dado que o jogador esta na tela de detalhes  
E nenhum jogo foi configurado no controlador  
Quando ele clicar no botao `Jogar`  
Entao o sistema nao deve navegar para nenhuma tela de jogo.

## US-05 - Adicionar um novo jogo

**Como** desenvolvedor,  
**eu quero** clicar no botao "Adicionar jogo" na tela inicial,  
**para que** eu adicione um jogo que criei.

**Prioridade:** Alta  
**Status:** Entregue  
**Issue/Card:** SCRUM-0  
**PR:**  
**Commit:** 
**Teste:** `GameDetailControllerMockTest`

### Criterios de aceitacao

**Cenario 1 - Navegar para o Campo Minado ao clicar em Jogar**  
Dado que o jogador esta na tela de detalhes do jogo `Campo Minado`  
Quando ele clicar no botao `Jogar`  
Entao o sistema deve navegar para a tela do Campo Minado  
E nenhuma outra tela de jogo deve ser aberta.

**Cenario 2 - Nao navegar quando nenhum jogo estiver configurado**  
Dado que o jogador esta na tela de detalhes  
E nenhum jogo foi configurado no controlador  
Quando ele clicar no botao `Jogar`  
Entao o sistema nao deve navegar para nenhuma tela de jogo.

## US-06 - Adicionar um novo jogo

**Como** desenvolvedor,  
**eu quero** clicar no botao "Adicionar jogo" na tela inicial,  
**para que** eu adicione um jogo que criei.

**Prioridade:** Alta  
**Status:** Entregue  
**Issue/Card:** SCRUM-0  
**PR:** #21
**Commit:** `fc9b7de3e2492aa171913c5c78566dda4c0697ed`
**Teste:** `deveAdicionarJogoNaLista`

### Criterios de aceitacao

**Cenario 1 - Abrir caixa de texto ao clicar em 'Novo Jogo'**  
Dado que o desenvolvedor esteja na tela inicial  
Quando ele clicar no botao `Novo Jogo`  
Entao o sistema deve abrir uma caixa de texto para digitar o nome do jogo  
E nenhuma outra tela de jogo deve acionável.

**Cenario 2 - Adicionar novo jogo ao digitar o nome do jogo**  
Dado que o desenvolvedor esteja na tela de adicionar jogo novo  
E digitar o nome do jogo
Quando ele clicar no botao `OK`  
O sistema deve adicionar o novo jogo à lista de jogos 