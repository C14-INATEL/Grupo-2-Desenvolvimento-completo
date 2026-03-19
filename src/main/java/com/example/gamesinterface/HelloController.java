package com.example.gamesinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {
    @FXML private Label textoBemVindo;
    @FXML private ListView<String> listaJogos;

    // Lista que armazena os jogos e atualiza a interface sozinha
    private ObservableList<String> jogos = FXCollections.observableArrayList("Jogo da Velha", "Campo Minado");
    private String nickUsuario = "Player 1";

    @FXML
    public void initialize() {
        listaJogos.setItems(jogos);
        textoBemVindo.setText("Bem-vindo, " + nickUsuario);
    }

    @FXML
    protected void onIniciarJogo() {
        String selecionado = listaJogos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            textoBemVindo.setText("Iniciando: " + selecionado);
        }
    }

    @FXML
    protected void onAdicionarJogo() {
        TextInputDialog dialog = new TextInputDialog("Novo Jogo");
        dialog.setHeaderText("Qual jogo deseja adicionar?");
        dialog.showAndWait().ifPresent(nome -> jogos.add(nome));
    }

    @FXML
    protected void onRemoverJogo() {
        int selectedIdx = listaJogos.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            jogos.remove(selectedIdx);
        }
    }

    @FXML
    protected void onTrocarNick() {
        TextInputDialog dialog = new TextInputDialog(nickUsuario);
        dialog.setHeaderText("Digite seu novo Nickname:");
        dialog.showAndWait().ifPresent(novoNick -> {
            nickUsuario = novoNick;
            textoBemVindo.setText("Bem-vindo, " + nickUsuario);
        });
    }
}