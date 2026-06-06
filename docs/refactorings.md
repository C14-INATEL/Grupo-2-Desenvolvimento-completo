# Refactorings

Este documento registra os refactorings aplicados ao longo do projeto, explicando o motivo de cada mudanca e apontando evidencias em commits, PRs ou cards do Scrum.

## RF-01 - Refinamento da interface e detalhamento dos menus

**Resumo:** refinamento visual e estrutural da interface do Game Hub, com dark theme minimalista, menu hamburger, pagina de detalhes dos jogos, icones e ajustes de tipografia.

**Motivo:** a interface inicial permitia acessar os jogos, mas ainda precisava comunicar melhor a proposta da plataforma e organizar a navegacao. O refinamento deixou o menu principal mais claro, criou uma etapa intermediaria de detalhes do jogo e melhorou a experiencia antes de iniciar uma partida.

**O que foi alterado:**

- Melhor organizacao visual do menu principal.
- Inclusao de menu hamburger/painel de configuracoes.
- Inclusao de tela de detalhes dos jogos antes de abrir a partida.
- Melhor uso de icones, descricoes e tipografia.
- Ajustes de navegacao para abrir as telas corretas a partir do menu e da tela de detalhes.

**Impacto no projeto:**

- Melhorou a usabilidade do Game Hub.
- Tornou o fluxo de navegacao mais consistente: menu -> detalhes -> jogo.
- Facilitou a explicacao das funcionalidades durante a defesa.
- Ajudou a separar melhor a responsabilidade entre menu, detalhe do jogo e telas especificas.

**Rastreabilidade:**

- **Autor:** MaskDMoa
- **Commit:** `bd10be6`
- **Data:** 24/03/2026
- **Mensagem do commit:** `Refinimento de Interface: dark theme minimalista, menu hamburger, pagina de detalhes dos jogos, icones e tipografia premium`
- **Card Scrum:** Documentar refactorings com rastreabilidade
- **Arquivos relacionados:** `menu-view.fxml`, `game-detail-view.fxml`, `app.css`, controllers de navegacao e telas de jogos

**Status:** Entregue
