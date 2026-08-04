package me.markerra.bridge;

import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class MinecraftBridgeClient extends WebSocketClient {
    public boolean isDialogActive = false;
    public int dialogSeconds = 0;

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
        System.out.println("[BridgeClient] Connected to the server. Waiting for the handshake...");
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonObject json = BridgeProtocol.GSON.fromJson(message, JsonObject.class);
            if (json == null || !json.has("type")) return;

            System.out.println("[BridgeClient] JSON: " + json);

            String type = json.get("type").getAsString();

            switch (type) {
                case "state" -> {
                    // Системные статусы сервера (Handshake)
                    BridgeProtocol.StateMessage stateMsg = BridgeProtocol.StateMessage.fromJson(message);
                    if (stateMsg.state() == null) return;

                    if ("connected".equals(stateMsg.state())) {
                        send(new BridgeProtocol.HelloMessage(role).toJson());
                    } else if ("ready".equals(stateMsg.state())) {
                        this.isReady = true;
                        System.out.println("[BridgeClient] Audio channel is opened");
                    }
                }

                case "dialog_state" -> {
                    BridgeProtocol.DialogStateEvent dialogEvent = BridgeProtocol.DialogStateEvent.fromJson(message);
                    this.dialogSeconds = dialogEvent.secondsPassed();
                    this.isDialogActive = dialogEvent.active();
                }
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