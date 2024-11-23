module client.chatclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.protobuf;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    exports client.chatclient.proto;
    opens client.chatclient.proto to javafx.fxml;

    exports client.chatclient.dto;
    opens client.chatclient.dto to javafx.fxml;

    exports client.chatclient;
    opens client.chatclient to javafx.fxml;

    exports client.chatclient.Static;
    opens client.chatclient.Static to javafx.fxml;

    exports client.chatclient.model;
    opens client.chatclient.model to javafx.fxml;

    exports client.chatclient.controller;
    opens client.chatclient.controller to javafx.fxml;

    exports client.chatclient.service;
    opens client.chatclient.service to javafx.fxml;
}