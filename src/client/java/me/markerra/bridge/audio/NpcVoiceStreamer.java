package me.markerra.bridge.audio;

public class NpcVoiceStreamer {

    private final PcmFrameQueue queue = new PcmFrameQueue();

    public void submitFrame(byte[] pcm) {
        queue.push(pcm);
    }

    public void tick() {
        PcmFrame frame = queue.poll();

        if (frame == null) {
            return;
        }

        // just logging

        System.out.println(
                "[NpcVoiceStreamer] PCM frame: "
                        + frame.size()
                        + " bytes"
        );
    }

    public int queuedFrames() {
        return queue.size();
    }

}