package me.markerra.bridge.audio;

public record PcmFrame(byte[] pcmData) {

    public int size() {
        return pcmData.length;
    }

}