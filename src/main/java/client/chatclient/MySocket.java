package client.chatclient;

import java.net.Socket;

public class MySocket {
    public final Socket clientSocket;

    public MySocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}

