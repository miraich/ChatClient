package client.chatclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatController {
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


    public ChatController() {
    }

    @FXML
    private void onEnterPressed(KeyEvent event) {
        if (chatService.getUser().getUsername() == null) {
            System.out.println("name is empty");
            return;
        }
        if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
            inputArea.appendText("\n");
            return;
        }
        String msg = inputArea.getText().trim();
        if (event.getCode() == KeyCode.ENTER && !msg.isEmpty()) {
            chatService.sendMessage(msg + "\n");
//            messagesArea.appendText(msg + "\n");
            inputArea.clear();
        }
    }

    @FXML
    private void onNameChanged() {
        chatService.getUser().setUsername(inputNameField.getText());
    }

//    @FXML
//    private void onConnect() {
//        if (chatService.getUser().getUsername() == null) {
//            System.out.println("name is empty");
//            return;
//        }
//
//    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
        inputNameField.setText(chatService.getUser().getUsername());
    }
}