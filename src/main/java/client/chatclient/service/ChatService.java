package client.chatclient.service;

import client.chatclient.MyIO;
import client.chatclient.dto.UserDTO;
import client.chatclient.model.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ChatService {
    @FXML
    private GridPane usersGrid;
    private final ObjectMapper objectMapper;
    private final User user;
    private final MyIO io;
    private volatile boolean runningMsgs = true;
    private volatile boolean runningUsernames = true;
    @FXML
    private TextArea messagesArea;

    public ChatService(User user, MyIO io) {
        this.user = user;
        this.io = io;
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper = new ObjectMapper(jsonFactory);
        beginReceiveUsers();
//        beginReceiveMessages();
    }

    public void sendMessage(String message) {
//        MessageOuterClass.User user1 = MessageOuterClass.User.newBuilder()
//                .setName(user.getUsername())
//                .build();
//
//        MessageOuterClass.Message um = MessageOuterClass.Message.newBuilder()
//                .setUser(user1)
//                .setMessage(message)
//                .setDateTime(LocalDate.now().toString())
//                .build();
//        try {
//            um.writeDelimitedTo(io.bos);
//            io.bos.flush();
//        } catch (IOException e) {
//            MyAlert.showAlert("Ошибка ввода/вывода", "Ошибка при отправки сообщения");
//        }
    }

    private void beginReceiveMessages() {
//        new Thread(() -> {
//            MessageOuterClass.Message um;
//            try {
//                while (runningMsgs && ((um = MessageOuterClass.Message.parseDelimitedFrom(io.bis)) != null)) {
//                    messagesArea.appendText(um.getUser().getName() + " " + um.getMessage());
//                }
//            } catch (IOException e) {
//                runningMsgs = false;
//            }
//        }).start();
    }

    private void beginReceiveUsers() {
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                UserDTO[] users;
                while (runningUsernames && io.bis.read(buffer) != -1) {
                    Platform.runLater(() -> usersGrid.getChildren().clear());
                    users = objectMapper.readValue(buffer, UserDTO[].class);
                    for (int i = 0; i < users.length; i++) {
                        int finalI = i;
                        UserDTO[] finalUsers = users;
                        Platform.runLater(() -> {
                            usersGrid.add(new Label(finalUsers[finalI].getUsername()), 0, finalI);
                            System.out.println(finalUsers[finalI].getUsername());
                        });
                    }
                }
            } catch (IOException e) {
                runningUsernames = false;
                System.out.println(e.getMessage());
            }
        }).start();
    }

    public void stopRunningMsgs() {
        runningMsgs = false;
    }

    public void stopRunningUsernames() {
        runningUsernames = false;
    }

    public User getUser() {
        return user;
    }

    public void setMessagesArea(TextArea messagesArea) {
        this.messagesArea = messagesArea;
    }

    public void setUsersGrid(GridPane usersGrid) {
        this.usersGrid = usersGrid;
    }
}
