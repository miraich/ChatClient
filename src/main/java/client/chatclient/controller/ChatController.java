package client.chatclient.controller;

import client.chatclient.Static.MyAlert;
import client.chatclient.model.User;
import client.chatclient.service.ChatService;
import client.chatclient.service.ConnectService;
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
    @FXML
    private Button changeNameButton;
    @FXML
    private TextField inputNameField;
    @FXML
    private TextArea messagesArea;
    @FXML
    private TextArea inputArea;

    private ChatService chatService;
    private ConnectService connectService;

    @FXML
    private void onEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !connectService.isConnected()) {
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
        if (event.getCode() == KeyCode.ENTER && !msg.isEmpty() && connectService.isConnected()) {
            chatService.sendMessage(msg + "\n");
            inputArea.clear();
        }
    }

    @FXML
    private void onNameChanged() {
        chatService.getUser().setUsername(inputNameField.getText().trim());
    }

    @FXML
    private void onConnectButtonPressed() {
        this.connectService.setConnectButton(connectButton);
        this.connectService.setupConnection(this);
    }

    public GridPane getUsersGrid() {
        return usersGrid;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public TextField getInputNameField() {
        return inputNameField;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
        this.chatService.setMessagesArea(messagesArea);
        this.chatService.setUsersGrid(usersGrid);
    }

    public void setConnectService(ConnectService connectService) {
        this.connectService = connectService;
    }
}