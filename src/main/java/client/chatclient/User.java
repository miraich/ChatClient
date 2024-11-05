package client.chatclient;

import java.io.*;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) {
            if (!username.isEmpty()) {
                if (username.length() > 15) {
                    System.out.println("Username too long");
                    return;
                }
                this.username = username;
                saveUserToFile();
            }
        }
    }

    private void saveUserToFile() {
        try (var oos = new ObjectOutputStream(new FileOutputStream("user.save"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    public static User loadUserFromFile() {
        User loadedUser = null;
        try (var ois = new ObjectInputStream(new FileInputStream("user.save"))) {
            loadedUser = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading user: " + e.getMessage());
        }
        return loadedUser;
    }
}
