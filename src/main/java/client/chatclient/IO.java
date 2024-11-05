package client.chatclient;

import java.io.*;

public class IO {
    public final DataInputStream dis;
    public final DataOutputStream dos;

    public IO(DataInputStream dis, DataOutputStream dos) {

        this.dis = dis;
        this.dos = dos;
    }

}
