package me.markerra.bridge;

import me.markerra.bridge.audio.NpcVoiceStreamer;
import me.markerra.voice.VoiceChatManager;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AudioBridge {
    private static final AtomicInteger framesReceived = new AtomicInteger(0);
    private static final AtomicInteger bytesReceived = new AtomicInteger(0);

    private static final AtomicReference<MinecraftBridgeClient> bridgeClient = new AtomicReference<>();

    public static void start() {
        try {
            if (bridgeClient.get() != null) {
                return;
            }

            URI uri = new URI("ws://127.0.0.1:25565" + BridgeProtocol.CHANNEL_BROWSER);

            NpcVoiceStreamer streamer = VoiceChatManager.getStreamer();
            MinecraftBridgeClient client = new MinecraftBridgeClient(
                uri,
                BridgeProtocol.ROLE_CONSUMER,
                pcmFrame -> {
                    streamer.submitFrame(pcmFrame);

                    int frames = framesReceived.incrementAndGet();
                    int bytes = bytesReceived.addAndGet(pcmFrame.length);

                    if (frames % 50 == 0) {
                        System.out.printf(
                                "[Minecraft Bridge] Frames=%d Bytes=%d Queue=%d%n",
                                frames,
                                bytes,
                                streamer.getQueue().size()
                        );
                    }
                }
            );

            bridgeClient.set(client);
            client.connect();

            System.out.println("[Minecraft Bridge] Connecting to bridge...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        try {
            bridgeClient.get().close();
            bridgeClient.set(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}