package client.chatclient;

import client.chatclient.controller.ChatController;
import client.chatclient.model.User;
import client.chatclient.service.ConnectService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 525, 375);
        ChatController controller = fxmlLoader.getController();
        controller.setConnectService(new ConnectService());
        controller.getInputNameField().setText(User.getInstance().getUsername());
        stage.setResizable(false);
        stage.setTitle("Чат Молодечно");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}