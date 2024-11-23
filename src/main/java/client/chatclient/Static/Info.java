package client.chatclient.Static;

import client.chatclient.AudioPlayer;
import client.chatclient.dto.UserDTO;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Info {
    public static final CopyOnWriteArraySet<UserDTO> activeUsers = new CopyOnWriteArraySet<>();
    public static final ConcurrentHashMap<InetAddress, AudioPlayer> usersAudio = new ConcurrentHashMap<>();
}
