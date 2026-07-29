package me.markerra.bridge.audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PcmFrameQueue {

    private final BlockingQueue<PcmFrame> queue = new LinkedBlockingQueue<>();

    public void push(byte[] pcm) {
        queue.offer(new PcmFrame(pcm));
    }

    public PcmFrame poll() {
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }

}