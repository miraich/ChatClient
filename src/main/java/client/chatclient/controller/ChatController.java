package client.chatclient.controller;

import client.chatclient.Static.MyAlert;
import client.chatclient.model.User;
import client.chatclient.service.ChatService;
import client.chatclient.service.ConnectionService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class ChatController {
    @FXML
    public GridPane usersGrid;
    @FXML
    private Button connectButton;
//    @FXML
//    private Button changeNameButton;
    @FXML
    private TextField inputNameField;
    @FXML
    private TextArea messagesArea;
    @FXML
    private TextArea inputArea;

    private ChatService chatService;
    private ConnectionService client;

    @FXML
    private void onEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !client.isConnected()) {
            MyAlert.showAlert("Ошибка ввода/вывода", "Вы не подключены к серверу");
            return;
        }
        if (event.getCode() == KeyCode.ENTER && User.getInstance().getUsername() == null
                && inputNameField.getText().isEmpty()) {
            MyAlert.showAlert("Ошибка ввода/вывода", "Имя не было введено");
            return;
        }
        if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
            inputArea.appendText("\n");
            return;
        }
        String msg = inputArea.getText().trim();
        if (msg.length() > 500) {
            MyAlert.showAlert("Ошибка ввода/вывода", "Сообщение превышает 500 символов");
            return;
        }
        if (event.getCode() == KeyCode.ENTER && !msg.isEmpty() && client.isConnected()) {
            chatService.sendMessage(msg + "\n");
            inputArea.clear();
        }
    }

    @FXML
    private void onNameChanged() {
        User.getInstance().setUsername(inputNameField.getText());
    }

    @FXML
    private void onConnectButtonPressed() {
        this.client.setConnectButton(connectButton);
        this.client.connect(this);
    }

//    public GridPane getUsersGrid() {
//        return usersGrid;
//    }
//
//    public ChatService getChatService() {
//        return chatService;
//    }

    public TextField getInputNameField() {
        return inputNameField;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
        this.chatService.setMessagesArea(messagesArea);
//        this.chatService.setUsersGrid(usersGrid);
    }

    public void setClient(ConnectionService client) {
        this.client = client;
    }
}