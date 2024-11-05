module client.chatclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens client.chatclient to javafx.fxml;
    exports client.chatclient;
}