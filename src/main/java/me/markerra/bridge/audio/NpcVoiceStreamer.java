package me.markerra.bridge.audio;

public class NpcVoiceStreamer {

    private final PcmFrameQueue queue = new PcmFrameQueue();

    public void submitFrame(byte[] pcm) {
        queue.push(pcm);
    }

    public PcmFrameQueue getQueue() {
        return queue;
    }

}