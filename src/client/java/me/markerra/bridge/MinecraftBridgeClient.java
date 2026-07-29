package me.markerra.bridge;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class MinecraftBridgeClient extends WebSocketClient {

    private final String role;
    private final Consumer<byte[]> onPcmFrameReceived;
    private boolean isReady = false;

    public MinecraftBridgeClient(URI serverUri, String role, Consumer<byte[]> onPcmFrameReceived) {
        super(serverUri);
        this.role = role;
        this.onPcmFrameReceived = onPcmFrameReceived;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("[BridgeClient] Подключено к серверу. Ожидание handshake...");
    }

    @Override
    public void onMessage(String message) {
        try {
            BridgeProtocol.StateMessage stateMsg = BridgeProtocol.StateMessage.fromJson(message);
            if (stateMsg == null || stateMsg.type() == null) return;

            // server sent connected, send role to server
            if ("connected".equals(stateMsg.state())) {
                send(new BridgeProtocol.HelloMessage(role).toJson());
            }
            // server sent 'ready', channel is opened
            else if ("ready".equals(stateMsg.state())) {
                isReady = true;
                System.out.println("[BridgeClient] Audio channel is opened");
            }
        } catch (Exception e) {
            System.err.println("[BridgeClient] Failed to read JSON: " + e.getMessage());
        }
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        if (!isReady) return;

        int expectedSize = BridgeProtocol.STANDARD_AUDIO.expectedFrameBytes();
        if (bytes.remaining() != expectedSize) {
            System.err.printf("[BridgeClient] Invalid frame size: got %d, expected %d%n",
                    bytes.remaining(), expectedSize);
            return;
        }

        byte[] pcm = new byte[bytes.remaining()];
        bytes.get(pcm);

        onPcmFrameReceived.accept(pcm);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        isReady = false;
        System.out.printf("[BridgeClient] Connection closed (%d): %s%n", code, reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("[BridgeClient] WebSocket Error: " + ex.getMessage());
    }
}