package client.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        ChatController chatController = fxmlLoader.getController();
        User user = User.loadUserFromFile();
        if (user == null) {
            user = new User();
        }

//        try {
//            Socket clientSocket = new Socket("localhost", 4444);
//            IO io = new IO(
//                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream())),
//                    new PrintWriter(clientSocket.getOutputStream())
//            );
//            chatController.setChatService(new ChatService(user, io));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        try {
            Socket clientSocket = new Socket("localhost", 4444);
            IO io = new IO(
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream())),
                    new BufferedOutputStream(clientSocket.getOutputStream())
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