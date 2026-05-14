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
