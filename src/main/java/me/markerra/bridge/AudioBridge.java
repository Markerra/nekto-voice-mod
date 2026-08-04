package me.markerra.bridge;

import me.markerra.bridge.audio.NpcVoiceStreamer;
import me.markerra.voice.VoiceChatManager;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AudioBridge {
    private static final AtomicInteger framesReceived = new AtomicInteger(0);
    private static final AtomicInteger bytesReceived = new AtomicInteger(0);

    private static final AtomicReference<MinecraftBridgeClient> consumerClient = new AtomicReference<>();
    private static final AtomicReference<MinecraftBridgeClient> sourceClient = new AtomicReference<>();

    public static void start() {
        try {
            if (consumerClient.get() != null || sourceClient.get() != null) {
                return;
            }

            // 1. consumer client, browser channel
            URI consumerUri = new URI("ws://127.0.0.1:25565" + BridgeProtocol.CHANNEL_BROWSER);
            NpcVoiceStreamer streamer = VoiceChatManager.getStreamer();

            MinecraftBridgeClient cClient = new MinecraftBridgeClient(
                    consumerUri,
                    BridgeProtocol.ROLE_CONSUMER,
                    pcmFrame -> {
                        streamer.submitFrame(pcmFrame);

                        int frames = framesReceived.incrementAndGet();
                        int bytes = bytesReceived.addAndGet(pcmFrame.length);

                        if (frames % 50 == 0) {
                            System.out.printf(
                                    "[AudioBridge] Frames=%d Bytes=%d Queue=%d%n",
                                    frames,
                                    bytes,
                                    streamer.getQueue().size()
                            );
                        }
                    }
            );
            consumerClient.set(cClient);
            cClient.connect();

            // 2. source client, channel game
            URI sourceUri = new URI("ws://127.0.0.1:25565" + BridgeProtocol.CHANNEL_GAME);

            MinecraftBridgeClient sClient = new MinecraftBridgeClient(
                    sourceUri,
                    BridgeProtocol.ROLE_SOURCE,
                    pcmFrame -> {}
            );
            sourceClient.set(sClient);
            sClient.connect();

            System.out.println("[AudioBridge] Connected Consumer to /browser and Source to /game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        try {
            if (consumerClient.get() != null) {
                consumerClient.get().close();
                consumerClient.set(null);
            }
            if (sourceClient.get() != null) {
                sourceClient.get().close();
                sourceClient.set(null);
            }
            System.out.println("[AudioBridge] Both clients stopped.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String jsonMessage) {
        MinecraftBridgeClient client = sourceClient.get() != null && sourceClient.get().isOpen()
                ? sourceClient.get()
                : consumerClient.get();

        if (client != null && client.isOpen()) {
            client.send(jsonMessage);
        } else {
            System.out.println("[AudioBridge] Can't send message, both WebSockets closed. Message: " + jsonMessage);
        }
    }

    public static void sendAction(BridgeProtocol.ActionMessage actionMessage) {
        sendMessage(actionMessage.toJson());
    }

    public static void sendBinaryAudio(byte[] pcmData) {
        MinecraftBridgeClient client = sourceClient.get();
        if (client != null && client.isOpen()) {
            client.send(pcmData);
        }
    }

    public static boolean isDialogActive() {
        if (consumerClient.get() == null) return false;
        return consumerClient.get().isDialogActive;
    }

    public static int getDialogSeconds() {
        if (consumerClient.get() == null) return 0;
        return consumerClient.get().dialogSeconds;
    }
}