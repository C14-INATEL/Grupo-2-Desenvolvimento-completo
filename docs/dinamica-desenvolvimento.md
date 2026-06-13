# Dinamica de Desenvolvimento

Registros de como o grupo organizou branches, commits, pull requests, decisoes tecnicas, bloqueios e reorganizacoes ao longo do desenvolvimento do Game Hub.

## Fluxo de branches

O grupo trabalhou com a branch `main` como base estavel do projeto. As novas funcionalidades, testes e ajustes foram desenvolvidos em branches separadas e depois integrados por pull request.

Principais branches usadas:

- `menu_jogos`: estrutura inicial da interface e organizacao do menu.
- `campo_minado`: logica inicial do Campo Minado.
- `batalha_naval`: primeira versao da logica de Batalha Naval.
- `feature/pedra-papel-tesoura`: logica e validacoes do jogo Pedra, Papel e Tesoura.
- `reorganizacao-estrutura`: reorganizacao do projeto, ajustes de Maven e inicio dos testes de menu.
- `testesUnitarios_logica`: testes de logica e interface.
- `Refinamento-Interface`: ajustes de interface, tela de detalhes e refinamento visual.
- `testesMocksMenu`: testes mock do `MenuController`.
- `test/teste_mine_sweeper`: testes mock do controller do Campo Minado.
- `test/testes-pedra-papel-tesoura`: testes mock do controller de Pedra, Papel e Tesoura.
- `preparandoJenkins`: criacao da pipeline inicial com Jenkins.

## Padrao de commits

Os commits seguiram, em grande parte, um padrao semantico simples:

- `feat`: criacao de funcionalidades.
- `fix`: correcao de problemas.
- `test` ou `tests`: criacao e ajuste de testes.
- `docs`: atualizacao de documentacao.
- `chore`: ajustes de configuracao, limpeza ou organizacao.
- `refactor`: reorganizacao interna sem mudanca direta de comportamento.
- `build`: alteracoes de dependencias ou configuracao de build.

Exemplos do historico:

- `6c4b0c5` - `build: Adcionando JavaFX e SQLite as dependencias`.
- `21c635c` - `feat: adicionando a logica do pedra, papel e tesoura`.
- `5801702` - `adicionado logica do campo minado`.
- `6827f02` - `feat: adicionando a primeira instancia do jogo de batalha naval`.
- `aca4d40` - `refactor: organiza estrutura de pastas e arquivos Maven`.
- `398cbda` - `tests: primeiros testes do menu`.
- `c4ac25b` - `test: adiciona testes para pedra papel e tesoura`.
- `0c3959f` - `test: adicionado testes unitarios com mock no minesweeper controller`.
- `1f604ff` - `test: adiciona testes unitarios com mock para RockPaperScissorsViewController`.
- `0d97eef` - `Update: Criacao da pipeline inicial`.

## Pull requests e revisao

O grupo integrou mudancas importantes por pull request, usando a branch `main` como destino. Isso permitiu separar entregas por tema e manter evidencias de revisao no GitHub.

Pull requests relevantes:

- `#2` - `menu_jogos`: primeira estrutura do menu e interface de jogos.
- `#3` - `campo_minado`: inclusao da logica do Campo Minado.
- `#4` e `#12` - `feature/pedra-papel-tesoura`: inclusao e evolucao do Pedra, Papel e Tesoura.
- `#5` e `#13` - `batalha_naval`: inclusao da logica e testes da Batalha Naval.
- `#6` - `reorganizacao-estrutura`: reorganizacao do projeto e inicio dos testes.
- `#8` - `testesUnitarios_logica`: testes de logica e interface.
- `#10` e `#17` - `test/testes-pedra-papel-tesoura`: testes e mocks relacionados a Pedra, Papel e Tesoura.
- `#11` e `#16` - `test/teste_mine_sweeper`: testes de model e controller do Campo Minado.
- `#14` - `Refinamento-Interface`: refinamento visual, tela de detalhes e correcao de cobertura.
- `#15` - `testesMocksMenu`: testes mock do menu.
- `#18` - `preparandoJenkins`: pipeline inicial com Jenkins.

Como melhoria para a defesa, o grupo deve mostrar no GitHub quais PRs tiveram aprovacao, comentarios ou discussao entre integrantes. Caso algum PR tenha sido apenas mergeado sem discussao, isso deve ser explicado como um ponto de melhoria do processo.

## Decisoes tecnicas

Durante o desenvolvimento, o grupo tomou algumas decisoes tecnicas importantes:

- Usar Java com JavaFX para criar uma aplicacao desktop de jogos.
- Usar Maven Wrapper para padronizar build e execucao sem depender de Maven instalado globalmente.
- Manter JUnit 5 como framework de testes.
- Separar parte da aplicacao em `controller`, `model`, `navigation` e arquivos FXML.
- Usar testes de model para regras de negocio e testes mock para controllers JavaFX.
- Criar um `GameNavigator` para reduzir acoplamento entre controllers e navegacao real da interface.
- Usar Jenkins como ferramenta de CI/CD, ja que GitHub Actions nao e permitido pelo enunciado.
- Iniciar a pipeline com `clean compile`, pois os testes JavaFX exigem configuracao grafica adicional quando executados em container Linux.

## Bloqueios encontrados

O historico mostra alguns bloqueios e dificuldades reais:

- Conflitos de merge e ajustes no `pom.xml`, evidenciados por commits como `fix: Corrigindo merge` e `Update: Corrigindo a merge entre os POMs`.
- Necessidade de reorganizar estrutura Maven e pastas do projeto apos as primeiras implementacoes.
- Limpeza de arquivos temporarios e pastas redundantes durante o refinamento da interface.
- Dificuldade de rodar testes JavaFX no Jenkins em container Linux por falta de bibliotecas graficas, como `libX11` e dependencias do JavaFX.
- Batalha Naval ficou inicialmente como logica isolada em `features`, diferente da organizacao dos outros jogos em `model` e `controller`. Na etapa final, essa pendencia foi corrigida com a criacao de uma tela propria, controller JavaFX, integracao com o menu/detalhes e testes de navegacao.

## Reorganizacoes realizadas

As principais reorganizacoes foram:

- Organizacao da estrutura Maven e compatibilidade entre dependencias.
- Reorganizacao da aplicacao para usar controllers, views FXML e navegacao centralizada.
- Criacao de tela de detalhes antes de abrir um jogo.
- Integracao da Batalha Naval ao fluxo visual do Game Hub, aproveitando a logica existente e adicionando controller, FXML e rota no `GameNavigator`.
- Separacao de testes unitarios e testes com mock.
- Inclusao de documentacao de execucao e testes no README.
- Criacao de documentacao de historias de usuario em `docs/historias-usuario.md`.
- Inclusao de Jenkinsfile e tutorial de Jenkins local no README.

## Linha do tempo resumida

- `16/03/2026`: estrutura inicial do projeto.
- `17/03/2026` a `18/03/2026`: inclusao de JavaFX, SQLite e dependencias.
- `19/03/2026`: criacao da interface inicial de jogos e reorganizacao de Maven.
- `20/03/2026`: inclusao das primeiras logicas de Campo Minado, Pedra, Papel e Tesoura e Batalha Naval.
- `24/03/2026`: reorganizacao da estrutura, inicio dos testes de menu e refinamento visual.
- `08/04/2026` a `10/04/2026`: ampliacao de testes, validacoes e refinamento da interface.
- `19/04/2026` a `01/05/2026`: foco em testes mock de menu, Campo Minado e Pedra, Papel e Tesoura.
- `14/05/2026` a `16/05/2026`: documentacao das historias de usuario.
- `21/05/2026`: criacao da pipeline inicial com Jenkins.
- `12/06/2026`: integracao da Batalha Naval como jogo completo na interface, com tabuleiro 10x10, tela propria e navegacao pelo menu.

## Licoes aprendidas

- Branches por funcionalidade ajudaram a organizar entregas diferentes, mas a padronizacao de nomes pode melhorar.
- PRs facilitaram rastreabilidade, mas e importante registrar comentarios e revisoes para evidenciar colaboracao.
- Commits semanticos ajudam a entender a evolucao do projeto.
- Testes de model sao mais simples de rodar em CI do que testes JavaFX.
- Decisoes de infraestrutura, como Jenkins, precisam considerar o ambiente onde a pipeline vai executar.
