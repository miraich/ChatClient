package client.chatclient.model;

import client.chatclient.Static.MyAlert;

import java.io.*;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private static User instance;

    public String getUsername() {
        return username;
    }

    private User() {
        User loadedUser = loadUserFromFile();
        if (loadedUser != null) {
            this.username = loadedUser.getUsername();
        }
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public void setUsername(String username) {
        if (username == null) {
            MyAlert.showAlert("Ошибка ввода/вывода", "Имя пользователя null");
            return;
        }
        username = username.trim();
        if (username.isEmpty()) {
            MyAlert.showAlert("Ошибка ввода/вывода", "Имя пользователя не содержит символов");
            return;
        }

        if (username.length() >= 15) {
            MyAlert.showAlert("Ошибка ввода/вывода", "Имя пользователя не должно быть длинее 15 символов");
            return;
        }
        this.username = username;
        saveUserToFile();
    }

    private void saveUserToFile() {
        try (var oos = new ObjectOutputStream(new FileOutputStream("user.save"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            MyAlert.showAlert("Ошибка сериализации пользователя", e.getMessage());
        }
    }

    public static User loadUserFromFile() {
        User loadedUser = null;
        try (var ois = new ObjectInputStream(new FileInputStream("user.save"))) {
            loadedUser = (User) ois.readObject();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            MyAlert.showAlert("Ошибка десериализации пользователя", e.getMessage());
        }
        return loadedUser;
    }
}
