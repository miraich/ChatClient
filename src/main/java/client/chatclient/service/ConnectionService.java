package client.chatclient.service;

import client.chatclient.MyIO;
import client.chatclient.Static.MyAlert;
import client.chatclient.controller.ChatController;
import client.chatclient.dto.UserDTO;
import client.chatclient.model.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    //    private Socket usersSocket;
    private Socket messagesSocket;
    //    private MyIO usersInfoIO;
    private MyIO messageInfoIO;
    private boolean connected;
    private final ObjectMapper objectMapper;

    public ConnectionService() {
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper = new ObjectMapper(jsonFactory);
    }

    public void connect(ChatController controller) {
        if (!connected) {
            try {
//                usersSocket = new Socket("26.73.93.155", 4444);
                messagesSocket = new Socket("192.168.0.107", 4450);
//                usersInfoIO = new MyIO(
//                        new BufferedInputStream(usersSocket.getInputStream()),
//                        new BufferedOutputStream(usersSocket.getOutputStream())
//                );
                messageInfoIO = new MyIO(
                        new BufferedInputStream(messagesSocket.getInputStream()),
                        new BufferedOutputStream(messagesSocket.getOutputStream())
                );
//                sendUser(new UserDTO(User.getInstance().getUsername()));
                controller.setChatService(new ChatService(messageInfoIO));
                Stage stage = (Stage) connectButton.getScene().getWindow();
                stage.setOnCloseRequest(_ -> {
                    try {
//                        usersInfoIO.bis.close();
                        messageInfoIO.bis.close();
//                        usersInfoIO.bos.close();
                        messageInfoIO.bos.close();
//                        usersSocket.close();
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
            disconnect(controller);
        }
    }

    private void disconnect(ChatController controller) {
        try {
//            usersInfoIO.bis.close();
            messageInfoIO.bis.close();
//            usersInfoIO.bos.close();
            messageInfoIO.bos.close();
//            usersSocket.close();
            messagesSocket.close();
            connected = false;
            controller.getUsersGrid().getChildren().clear();
            connectButton.setText("Подключиться");
        } catch (IOException e) {
            MyAlert.showAlert("Ошибка при закрытии соединения", e.getMessage());
        }
    }

    private void sendUser(UserDTO user) {
        new Thread(() -> {
            try {
                byte[] json_b = objectMapper.writeValueAsBytes(user);
//                usersInfoIO.bos.write(json_b);
//                usersInfoIO.bos.flush();
            } catch (IOException e) {
                MyAlert.showAlert("Ошибка ввода/вывода", "Ошибка при отправки объекта пользователя");
            }
        }).start();
    }

    public void setConnectButton(Button connectButton) {
        this.connectButton = connectButton;
    }

    public boolean isConnected() {
        return connected;
    }


}
