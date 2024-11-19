package client.chatclient.Static;

import javafx.scene.control.Alert;

public class MyAlert {
    public static void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
