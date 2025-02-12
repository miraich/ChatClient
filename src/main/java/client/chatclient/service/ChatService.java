package client.chatclient.service;

import client.chatclient.AudioPlayer;
import client.chatclient.MyIO;
import client.chatclient.Static.DateTime;
import client.chatclient.dto.MessageDTO;
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
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static client.chatclient.Static.Info.activeUsers;
import static client.chatclient.Static.Info.usersAudio;

public class ChatService {
    private DatagramSocket socketAudio;
    private final AudioFormat format;
    private final DataLine.Info info;
//    private final MyIO usersInfoIO;
    private final MyIO messageInfoIO;
    private final ObjectMapper objectMapper;
    private final byte[] audioBufferWriting = new byte[4096];
    private final byte[] audioBufferListening = new byte[4096];
    private final byte[] messageBuffer = new byte[1024];
    @FXML
    private GridPane usersGrid;
    @FXML
    private TextArea messagesArea;

    public ChatService(MyIO messageInfoIO) {
//        this.usersInfoIO = usersInfoIO;
        this.messageInfoIO = messageInfoIO;
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper = new ObjectMapper(jsonFactory);
        this.format = new AudioFormat(44100, 16, 1, true, true);
        this.info = new DataLine.Info(TargetDataLine.class, format);
        try {
            socketAudio = new DatagramSocket(50036);
        } catch (SocketException e) {
            e.printStackTrace();
        }
//        beginReceiveUsers();
        beginReceiveMessages();
//        beginSendingAudio();
//        beginRecievingAudio();
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
//                MyAlert.showAlert("Ошибка ввода/вывода", "Ошибка при отправки сообщения");
            }
        }).start();
    }

    private void beginReceiveMessages() {
        new Thread(() -> {
            try {
                MessageDTO messageDTO;
                while (messageInfoIO.bis.read(messageBuffer, 0, messageBuffer.length) != -1) {
                    messageDTO = objectMapper.readValue(messageBuffer, MessageDTO.class);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder
                            .append(messageDTO.getDateTime())
                            .append(" ")
                            .append(messageDTO.getUser().getUsername())
                            .append(" ")
                            .append(messageDTO.getMessage());
                    messagesArea.appendText(stringBuilder.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

//    private void beginReceiveUsers() {
//        new Thread(() -> {
//            try {
//                UserDTO[] users;
//                while (usersInfoIO.bis.read(messageBuffer, 0, messageBuffer.length) != -1) {
//                    users = objectMapper.readValue(messageBuffer, UserDTO[].class);
//                    activeUsers.clear();
//                    Platform.runLater(() -> usersGrid.getChildren().clear());
//                    activeUsers.addAll(Arrays.asList(users));
//                    System.out.println(activeUsers.size());
//                    int i = 0;
//                    for (UserDTO user : activeUsers) {
//                        String name = user.getUsername();
//                        int finalI = i;
//                        Platform.runLater(() -> usersGrid.add(new Label(name), 0, finalI));
//                        i++;
//                    }
////                    messagesArea.appendText(new StringBuilder("Пользователь ")
////                            .append(name)
////                            .append(" ")
////                            .append("присоединился к серверу!\n")
////                            .toString());
//                }
//            } catch (IOException e) {
////                Platform.runLater(() -> MyAlert.showAlert("Ошибка ввода/вывода", "Ошибка при получении имен пользователей"));
//                e.printStackTrace();
//            }
//        }).start();
//    }

    private void beginSendingAudio() {
        new Thread(() -> {
            try {
                TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
                targetLine.open(format);
                targetLine.start();
                System.out.println("Recording and sending audio...");
                while (true) { //флаг для кнопки мута
                    int bytesRead = targetLine.read(audioBufferWriting, 0, audioBufferWriting.length);
                    DatagramPacket packet = new DatagramPacket(audioBufferWriting, bytesRead, InetAddress.getByName("localhost"), 50005);
                    socketAudio.send(packet);
                }

            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void beginRecievingAudio() {
        new Thread(() -> {
            try {
                while (true) {
                    DatagramPacket audioPacket = new DatagramPacket(audioBufferListening, audioBufferListening.length);
                    socketAudio.receive(audioPacket);

                    // Логируем адрес отправителя
                    System.out.println("Received audio from: " + audioPacket.getAddress() + ":" + audioPacket.getPort());

                    if (audioPacket.getPort() == 50030) {
                        continue; // Пропускаем свой собственный аудиопоток
                        // не правильно мб
                    }


                    byte[] audioData = audioPacket.getData();
                    int length = audioPacket.getLength();

                    AudioPlayer audioPlayer = usersAudio.get(audioPacket.getAddress());
                    if (audioPlayer == null) {
                        audioPlayer = new AudioPlayer();
                        usersAudio.put(audioPacket.getAddress(), audioPlayer);
                    }
                    AudioPlayer finalAudioPlayer = audioPlayer;
                    new Thread(() -> finalAudioPlayer.playAudio(audioData, length)).start();
                }

            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setMessagesArea(TextArea messagesArea) {
        this.messagesArea = messagesArea;
    }

    public void setUsersGrid(GridPane usersGrid) {
        this.usersGrid = usersGrid;
    }
}
