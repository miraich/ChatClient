package client.chatclient;

import java.io.*;

public class MyIO {
    public final BufferedInputStream bis;
    public final BufferedOutputStream bos;

    public MyIO(BufferedInputStream bis, BufferedOutputStream bos) {
        this.bis = bis;
        this.bos = bos;
    }
}
