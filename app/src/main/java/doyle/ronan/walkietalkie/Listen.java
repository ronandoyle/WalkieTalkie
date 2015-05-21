package doyle.ronan.walkietalkie;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This class is used to receive and play an audio stream from another Android device.
 *
 * Created by Ronan on 21/05/2015.
 */
public class Listen extends AsyncTask<Void, Integer, Void> {

    private MediaPlayer mp;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private int port = 50005;
    private int sampleRate = 44100;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    private AudioTrack audioTrack;

    public Listen() {
    }

    public void playStream() {
        doInBackground();
    }

    @Override
    protected Void doInBackground(Void... params) {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        byte[] receiveData = new byte[2200];

        try {
            audioTrack = new AudioTrack(AudioTrack.MODE_STREAM, sampleRate, 1,
                    audioFormat, minBufSize, AudioTrack.MODE_STREAM);
            audioTrack.play();
            socket = new DatagramSocket(port);
            packet = new DatagramPacket(receiveData, receiveData.length);

            while (true) {
                socket.receive(packet);
                audioTrack.write(packet.getData(), 0, packet.getLength());
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
