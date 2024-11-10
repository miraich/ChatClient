package client.chatclient;

import java.io.IOException;

public class ChatService {
    private final User user;
    private final IO io;

    public ChatService(User user, IO io) {
        this.user = user;
        this.io = io;
    }

    public void sendMessage(String message) {
        try {
            io.bos.write(message.getBytes());
            io.bos.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(message + " sent");
    }

    public void receiveMessage(String message) {

    }

    public User getUser() {
        return user;
    }
}
