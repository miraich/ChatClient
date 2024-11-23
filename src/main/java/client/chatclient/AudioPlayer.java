package client.chatclient;

import javax.sound.sampled.*;

public class AudioPlayer {
    private final SourceDataLine line;

    public AudioPlayer() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
    }

    public void playAudio(byte[] audioData, int length) {
        line.write(audioData, 0, length);
        line.drain();
    }

    public void close() {
        line.stop();
        line.close();
    }
}
