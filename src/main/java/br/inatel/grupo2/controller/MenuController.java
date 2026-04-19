package br.inatel.grupo2.controller;

import br.inatel.grupo2.navigation.GameNavigator;
import br.inatel.grupo2.navigation.JavaFxGameNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public class MenuController {
    @FXML private Label tituloTela;
    @FXML private Label textoBemVindo;
    @FXML private Label statusLabel;
    @FXML private ListView<String> listaJogos;

    // Settings panel
    @FXML private VBox settingsPanel;
    @FXML private Pane settingsOverlay;
    @FXML private StackPane profileCircle;
    @FXML private Label profileInitials;
    @FXML private Label nickLabel;

    private final ObservableList<String> jogos = FXCollections.observableArrayList(
            "\uD83E\uDEA8  Pedra, Papel e Tesoura",
            "\uD83D\uDCA3  Campo Minado",
            "\u274C  Jogo da Velha"
    );

    // Map display name -> real name
    private static final Map<String, String> GAME_NAMES = Map.of(
            "\uD83E\uDEA8  Pedra, Papel e Tesoura", "Pedra, Papel e Tesoura",
            "\uD83D\uDCA3  Campo Minado", "Campo Minado",
            "\u274C  Jogo da Velha", "Jogo da Velha"
    );

    private static final Map<String, String> GAME_ICONS = Map.of(
            "Pedra, Papel e Tesoura", "\uD83E\uDEA8\u2702\uFE0F\uD83D\uDCC4",
            "Campo Minado", "\uD83D\uDCA3",
            "Jogo da Velha", "\u274C\u2B55"
    );

    private static final Map<String, String> GAME_DESCRIPTIONS = Map.of(
            "Pedra, Papel e Tesoura", "O cl\u00E1ssico jogo de estrat\u00E9gia! Escolha pedra, papel ou tesoura e desafie o computador. Acompanhe seu placar em tempo real.",
            "Campo Minado", "Encontre todas as minas escondidas sem detonar nenhuma. Teste sua l\u00F3gica e habilidade!",
            "Jogo da Velha", "Marque tres simbolos em linha para vencer. Jogue contra o computador ou desafie outra pessoa no mesmo teclado."
    );

    private String nickUsuario = "Player 1";
    private GameNavigator navigator = new JavaFxGameNavigator();

    @FXML
    public void initialize() {
        listaJogos.setItems(jogos);
        listaJogos.getSelectionModel().selectFirst();
        atualizarBoasVindas();
        atualizarPerfil();
        statusLabel.setText("Selecione um jogo e clique em Jogar.");
    }

    @FXML
    protected void onIniciarJogo() {
        String selecionado = listaJogos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            statusLabel.setText("Selecione um jogo para continuar.");
            return;
        }

        String realName = GAME_NAMES.getOrDefault(selecionado, selecionado);
        String icon = GAME_ICONS.getOrDefault(realName, "\uD83C\uDFAE");
        String desc = GAME_DESCRIPTIONS.getOrDefault(realName, "Sem descri\u00E7\u00E3o dispon\u00EDvel.");

        navigator.showGameDetailScreen(realName, icon, desc);
    }

    @FXML
    protected void onAdicionarJogo() {
        TextInputDialog dialog = new TextInputDialog("Novo Jogo");
        dialog.setHeaderText("Qual jogo deseja adicionar?");
        dialog.showAndWait()
                .map(String::trim)
                .filter(nome -> !nome.isEmpty())
                .ifPresent(nome -> {
                    jogos.add("\uD83C\uDFAE  " + nome);
                    listaJogos.getSelectionModel().selectLast();
                    statusLabel.setText("Jogo adicionado \u00E0 lista.");
                });
    }

    @FXML
    protected void onRemoverJogo() {
        int selectedIdx = listaJogos.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            jogos.remove(selectedIdx);
            statusLabel.setText("Jogo removido da lista.");
        } else {
            statusLabel.setText("Selecione um jogo para remover.");
        }
    }

    @FXML
    protected void onTrocarNick() {
        solicitarNovoNick().ifPresent(novoNick -> {
            nickUsuario = novoNick;
            atualizarBoasVindas();
            atualizarPerfil();
            statusLabel.setText("Nickname atualizado.");
        });
    }

    @FXML
    protected void onMudarImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher imagem de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(settingsPanel.getScene().getWindow());
        if (file != null) {
            statusLabel.setText("Imagem selecionada: " + file.getName());
        }
    }

    @FXML
    protected void onToggleSettings() {
        boolean isVisible = settingsPanel.isVisible();
        settingsPanel.setVisible(!isVisible);
        settingsOverlay.setVisible(!isVisible);
    }

    protected void abrirPedraPapelTesoura() {
        navigator.showRockPaperScissorsScreen();
    }

    public void setNavigator(GameNavigator navigator) {
        this.navigator = navigator;
    }

    protected Optional<String> solicitarNovoNick() {
        TextInputDialog dialog = new TextInputDialog(nickUsuario);
        dialog.setHeaderText("Digite seu novo Nickname:");
        return dialog.showAndWait()
                .map(String::trim)
                .filter(novoNick -> !novoNick.isEmpty());
    }

    private void atualizarBoasVindas() {
        textoBemVindo.setText("Bem-vindo, " + nickUsuario);
    }

    private void atualizarPerfil() {
        if (nickLabel != null) {
            nickLabel.setText(nickUsuario);
        }
        if (profileInitials != null) {
            String initials = nickUsuario.length() >= 2
                    ? nickUsuario.substring(0, 2).toUpperCase()
                    : nickUsuario.toUpperCase();
            profileInitials.setText(initials);
        }
    }
}
