package client.chatclient;

import java.io.*;
import java.net.Socket;

public class Client {
    public Client() {

    }

    public void Run() {
        File file = new File("bg3.exe");
        try (var clientSocket = new Socket("localhost", 4444);
             var dos = new DataOutputStream(clientSocket.getOutputStream());
             var dis = new DataInputStream(new FileInputStream(file.getName()))) {

            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);  // Передаем только те байты, которые реально прочитаны
            }
            System.out.println("Client sent to server");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
