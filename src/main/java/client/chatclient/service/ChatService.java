package client.chatclient.service;

import client.chatclient.MyIO;
import client.chatclient.Static.DateTime;
import client.chatclient.Static.MyAlert;
import client.chatclient.dto.MessageDTO;
import client.chatclient.dto.UserDTO;
import client.chatclient.model.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.ZonedDateTime;


public class ChatService {
    private final MyIO messageInfoIO;
    private final ObjectMapper objectMapper;
    private final byte[] messageBuffer = new byte[1024];
    //    @FXML
//    private GridPane usersGrid;
    @FXML
    private TextArea messagesArea;

    public ChatService(MyIO messageInfoIO) {
        this.messageInfoIO = messageInfoIO;
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper = new ObjectMapper(jsonFactory);
        beginReceiveMessages();
    }

    public void sendMessage(String message) {
        new Thread(() -> {
            try {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setMessage(message);
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(User.getInstance().getUsername());
                messageDTO.setUser(userDTO);
                DateTime.currentTime = ZonedDateTime.now();
                messageDTO.setDateTime(DateTime.currentTime.format(DateTime.formatter));
                byte[] json_b = objectMapper.writeValueAsBytes(messageDTO);
                messageInfoIO.bos.write(json_b);
                messageInfoIO.bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                MyAlert.showAlert("Ошибка ввода/вывода", "Ошибка при отправки сообщения");
            }
        }).start();
    }

    private void beginReceiveMessages() {
        new Thread(() -> {
            try {
                MessageDTO messageDTO; // подумать StringBuilder
                while (messageInfoIO.bis.read(messageBuffer, 0, messageBuffer.length) != -1) {
                    messageDTO = objectMapper.readValue(messageBuffer, MessageDTO.class);
                    String stringBuilder = messageDTO.getDateTime() +
                            " " +
                            messageDTO.getUser().getUsername() +
                            " " +
                            messageDTO.getMessage();
                    messagesArea.appendText(stringBuilder);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setMessagesArea(TextArea messagesArea) {
        this.messagesArea = messagesArea;
    }

//    public void setUsersGrid(GridPane usersGrid) {
//        this.usersGrid = usersGrid;
//    }
}
