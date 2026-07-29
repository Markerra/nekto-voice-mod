package me.markerra.voice.audio;

import java.util.function.Supplier;

public class SineWaveGenerator implements Supplier<short[]> {

    private static final int SAMPLE_RATE = 48000;
    private static final int FRAME_SIZE = 960;

    private static final double FREQUENCY = 440D;

    private double phase = 0D;

    @Override
    public short[] get() {

        short[] samples = new short[FRAME_SIZE];

        for (int i = 0; i < FRAME_SIZE; i++) {

            samples[i] = (short) (Math.sin(phase) * 12000);

            phase += 2D * Math.PI * FREQUENCY / SAMPLE_RATE;

            if (phase > Math.PI * 2D) {
                phase -= Math.PI * 2D;
            }

        }

        return samples;
    }

}