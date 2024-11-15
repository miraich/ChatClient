package client.chatclient;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class ChatService {
    private final User user;
    private final MyIO io;
    private volatile boolean running = true;
    @FXML
    private TextArea messagesArea;

    public ChatService(User user, MyIO io) {
        this.user = user;
        this.io = io;
        beginReceiveMessages();
    }

    public void sendMessage(String message) {
        try {
            io.bos.write(user.getUsername().getBytes());
            io.bos.write('|');
            io.bos.write(message.getBytes());
            io.bos.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void beginReceiveMessages() {
        new Thread(() -> {
            int bytesRead;
            byte[] buffer = new byte[4192];
            try {
                while (running && ((bytesRead = io.bis.read(buffer)) != -1)) {
                    String received = new String(buffer, 0, bytesRead);
                    String[] splitted = received.split("\\|");
                    splitted[1] += "\n";
                    String msg = String.join(" ", splitted);
                    messagesArea.appendText(msg);
                }
            } catch (IOException e) {
                running = false;
                System.out.println(e.getMessage());
            }
        }).start();
    }

    public void stop() {
        running = false;
    }

    public User getUser() {
        return user;
    }

    public void setMessagesArea(TextArea messagesArea) {
        this.messagesArea = messagesArea;
    }
}
