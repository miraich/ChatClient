package client.chatclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatService {
    private final User user;
    private final IO io;

    public ChatService(User user, IO io) {
        this.user = user;
        this.io = io;
    }

    public void sendMessage(String message) {
        try {
            io.dos.writeUTF(message);
            System.out.println(message + " sent");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void receiveMessage(String message) {

    }

    public User getUser() {
        return user;
    }
}
