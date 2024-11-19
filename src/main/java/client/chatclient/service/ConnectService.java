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

public class ConnectService {

    private final ObjectMapper objectMapper;
    @FXML
    private Button connectButton;
    private Socket clientSocket;
    private MyIO io;
    private boolean connected;

    public ConnectService() {
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper = new ObjectMapper(jsonFactory);
    }

    public void setupConnection(ChatController controller) {
        if (!connected) {
            try {
                clientSocket = new Socket("192.168.0.107", 4444);
                io = new MyIO(
                        new BufferedInputStream(clientSocket.getInputStream()),
                        new BufferedOutputStream(clientSocket.getOutputStream())
                );
                sendUser(new UserDTO(User.getInstance().getUsername()));
                controller.setChatService(new ChatService(User.getInstance(), io));
                Stage stage = (Stage) connectButton.getScene().getWindow();
                stage.setOnCloseRequest(_ -> {
                    try {
                        controller.getChatService().stopRunningMsgs();
                        controller.getChatService().stopRunningUsernames();
                        io.bis.close();
                        io.bos.close();
                        clientSocket.close();
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
            controller.getChatService().stopRunningUsernames();
            controller.getChatService().stopRunningMsgs();
            io.bis.close();
            io.bos.close();
            clientSocket.close();
            connected = false;
            controller.getUsersGrid().getChildren().clear();
            connectButton.setText("Подключиться");
        } catch (IOException e) {
            MyAlert.showAlert("Ошибка при закрытии соединения", e.getMessage());
        }
    }

    private void sendUser(UserDTO user) {
        try {
            byte[] json_b = objectMapper.writeValueAsBytes(user);
            io.bos.write(json_b);
            io.bos.flush();
        } catch (IOException e) {
            MyAlert.showAlert("Ошибка ввода/вывода", "Ошибка при отправки объекта пользователя");
        }
    }

    public void setConnectButton(Button connectButton) {
        this.connectButton = connectButton;
    }

    public boolean isConnected() {
        return connected;
    }
}
