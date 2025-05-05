package client.chatclient.service;

import client.chatclient.MyIO;
import client.chatclient.Static.MyAlert;
import client.chatclient.controller.ChatController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionService {

    @FXML
    private Button connectButton;
    private Socket messagesSocket;
    private MyIO messageInfoIO;
    private boolean connected;

    public void connect(ChatController controller) {
        if (!connected) {
            try {
                messagesSocket = new Socket("192.168.0.107", 4450);
                messageInfoIO = new MyIO(
                        new BufferedInputStream(messagesSocket.getInputStream()),
                        new BufferedOutputStream(messagesSocket.getOutputStream())
                );
                controller.setChatService(new ChatService(messageInfoIO));
                Stage stage = (Stage) connectButton.getScene().getWindow();
                stage.setOnCloseRequest(_ -> {
                    try {
                        messageInfoIO.bis.close();
                        messageInfoIO.bos.close();
                        messagesSocket.close();
                    } catch (IOException e) {
                        MyAlert.showAlert("Ошибка при закрытии соединения", e.getMessage());
                    }
                });
                connected = true;
                connectButton.setText("Отключиться");
            } catch (IOException e) {
                MyAlert.showAlert("Ошибка при установлении соединения", e.getMessage());
            }
        } else {
            disconnect();
        }
    }

    private void disconnect() {
        try {
            messageInfoIO.bis.close();
            messageInfoIO.bos.close();
            messagesSocket.close();
            connected = false;
//            controller.getUsersGrid().getChildren().clear();
            connectButton.setText("Подключиться");
        } catch (IOException e) {
            MyAlert.showAlert("Ошибка при закрытии соединения", e.getMessage());
        }
    }

    public void setConnectButton(Button connectButton) {
        this.connectButton = connectButton;
    }

    public boolean isConnected() {
        return connected;
    }
}
