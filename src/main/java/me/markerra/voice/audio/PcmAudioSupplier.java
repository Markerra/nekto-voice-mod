package me.markerra.voice.audio;

import me.markerra.bridge.audio.PcmFrame;
import me.markerra.bridge.audio.PcmFrameQueue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Supplier;

public class PcmAudioSupplier implements Supplier<short[]> {

    public static final int FRAME_SAMPLES = 960;

    private final PcmFrameQueue queue;

    public PcmAudioSupplier(PcmFrameQueue queue) {
        this.queue = queue;
    }

    @Override
    public short[] get() {

        PcmFrame frame = queue.poll();

        if (frame == null) {
            return silence();
        }

        byte[] pcm = frame.pcmData();

        if (pcm.length != FRAME_SAMPLES * 2) {
            System.err.println("[VoiceChat] Invalid PCM frame size: " + pcm.length);
            return silence();
        }

        short[] samples = new short[FRAME_SAMPLES];

        ByteBuffer.wrap(pcm)
                .order(ByteOrder.LITTLE_ENDIAN)
                .asShortBuffer()
                .get(samples);

        return samples;
    }

    private short[] silence() {
        return new short[FRAME_SAMPLES];
    }

}