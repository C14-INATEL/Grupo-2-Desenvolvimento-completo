package br.inatel.grupo2.controller;

import br.inatel.grupo2.app.GameHubApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class MenuController {
    @FXML private Label tituloTela;
    @FXML private Label textoBemVindo;
    @FXML private Label statusLabel;
    @FXML private ListView<String> listaJogos;

    private final ObservableList<String> jogos = FXCollections.observableArrayList(
            "Pedra, Papel e Tesoura",
            "Campo Minado",
            "Jogo da Velha"
    );
    private String nickUsuario = "Player 1";

    @FXML
    public void initialize() {
        listaJogos.setItems(jogos);
        listaJogos.getSelectionModel().selectFirst();
        atualizarBoasVindas();
        statusLabel.setText("Selecione um jogo e clique em Jogar.");
    }

    @FXML
    protected void onIniciarJogo() {
        String selecionado = listaJogos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            statusLabel.setText("Selecione um jogo para continuar.");
            return;
        }

        switch (selecionado) {
            case "Pedra, Papel e Tesoura" -> abrirPedraPapelTesoura();
            default -> statusLabel.setText("error");
        }
    }

    @FXML
    protected void onAdicionarJogo() {
        TextInputDialog dialog = new TextInputDialog("Novo Jogo");
        dialog.setHeaderText("Qual jogo deseja adicionar?");
        dialog.showAndWait()
                .map(String::trim)
                .filter(nome -> !nome.isEmpty())
                .ifPresent(nome -> {
                    jogos.add(nome);
                    listaJogos.getSelectionModel().select(nome);
                    statusLabel.setText("Jogo adicionado a lista.");
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
            statusLabel.setText("Nickname atualizado.");
        });
    }

    protected void abrirPedraPapelTesoura() {
        GameHubApplication.showRockPaperScissorsScreen();
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
}
