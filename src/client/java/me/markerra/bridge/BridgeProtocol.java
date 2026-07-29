package me.markerra.bridge;

import com.google.gson.Gson;

public final class BridgeProtocol {
    public static final Gson GSON = new Gson();

    // endpoints
    public static final String CHANNEL_GAME = "/game";
    public static final String CHANNEL_BROWSER = "/browser";

    // Роли клиентов
    public static final String ROLE_SOURCE = "source";
    public static final String ROLE_CONSUMER = "consumer";

    // Стандарт аудиоформата (48 кГц, 1 моно-канал, 16 бит, блоки по 20 мс)
    public record AudioSpec(int sampleRate, int channels, int bitsPerSample, int frameDurationMs) {
        public int expectedFrameBytes() {
            return (sampleRate * frameDurationMs / 1000) * channels * (bitsPerSample / 8);
        }
    }

    public static final AudioSpec STANDARD_AUDIO = new AudioSpec(48000, 1, 16, 20);

    // JSON-сообщение приветствия (Java 21 Record)
    public record HelloMessage(String type, String role) {
        public HelloMessage(String role) {
            this("hello", role);
        }
        public String toJson() {
            return GSON.toJson(this);
        }
    }

    // JSON-сообщение ответа от сервера (состояние)
    public record StateMessage(String type, String state, String message) {
        public static StateMessage fromJson(String json) {
            return GSON.fromJson(json, StateMessage.class);
        }
    }

    private BridgeProtocol() {}
}