package client.chatclient;

import java.io.*;

public class IO {
    public final BufferedReader br;
    public final BufferedOutputStream bos;

    public IO(BufferedReader br, BufferedOutputStream bos) {

        this.br = br;
        this.bos = bos;
    }
}
