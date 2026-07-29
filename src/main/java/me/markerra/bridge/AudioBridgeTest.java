package me.markerra.bridge;

import me.markerra.bridge.audio.NpcVoiceStreamer;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AudioBridgeTest {
    private static final AtomicInteger framesReceived = new AtomicInteger(0);
    private static final AtomicInteger bytesReceived = new AtomicInteger(0);

    private static final AtomicReference<MinecraftBridgeClient> bridgeClient = new AtomicReference<>();

    public static void runTest() {
        try {
            if (bridgeClient.get() != null) {
                return;
            }

            URI uri = new URI("ws://127.0.0.1:25565" + BridgeProtocol.CHANNEL_BROWSER);

            NpcVoiceStreamer streamer = new NpcVoiceStreamer();
            MinecraftBridgeClient client = new MinecraftBridgeClient(
                uri,
                BridgeProtocol.ROLE_CONSUMER,
                pcmFrame -> {
                    streamer.submitFrame(pcmFrame);

                    int frames = framesReceived.incrementAndGet();
                    int bytes = bytesReceived.addAndGet(pcmFrame.length);

                    if (frames % 50 == 0) {
                        System.out.printf(
                                "[Minecraft Test] Frames=%d Bytes=%d Queue=%d%n",
                                frames,
                                bytes,
                                streamer.queuedFrames()
                        );
                    }
                }
            );

            bridgeClient.set(client);
            client.connect();

            System.out.println("[Minecraft Test] Connecting to bridge...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopTest() {
        try {
            bridgeClient.get().close();
            bridgeClient.set(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}