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

        try {
            Socket clientSocket = new Socket("26.73.93.155", 4444);
            MyIO io = new MyIO(
                    new BufferedInputStream(clientSocket.getInputStream()),
                    new BufferedOutputStream(clientSocket.getOutputStream())
            );
            chatController.setChatService(new ChatService(user, io));

            stage.setOnCloseRequest(_ -> {
                try {
                    chatController.getChatService().stop();
                    io.bos.close();
                    io.bis.close();
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        } catch (IOException e) {
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