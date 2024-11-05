package client.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        ChatController chatController = fxmlLoader.getController();
        User user = User.loadUserFromFile();
        if (user == null) {
            user = new User();
        }

        try {
            MySocket mySocket = new MySocket(new Socket("localhost", 4444));
            IO io = new IO(
                    new DataInputStream(mySocket.clientSocket.getInputStream()),
                    new DataOutputStream(mySocket.clientSocket.getOutputStream())
            );
            chatController.setChatService(new ChatService(user, io));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        stage.setTitle("Чат Молодечно");
        stage.setScene(scene);
        stage.show();
//        Dialog<String> dialog = new Dialog<>();
//        dialog.setTitle("Hello World");
//        dialog.setHeaderText("Hello World");
//        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}