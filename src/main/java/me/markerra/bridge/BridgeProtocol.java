package me.markerra.bridge;

import com.google.gson.Gson;

public final class BridgeProtocol {
    public static final Gson GSON = new Gson();

    // endpoints
    public static final String CHANNEL_GAME = "/game";
    public static final String CHANNEL_BROWSER = "/browser";

    // roles
    public static final String ROLE_SOURCE = "source";
    public static final String ROLE_CONSUMER = "consumer";

    // Стандарт аудиоформата (48 кГц, 1 моно-канал, 16 бит, блоки по 20 мс)
    public record AudioSpec(int sampleRate, int channels, int bitsPerSample, int frameDurationMs) {
        public int expectedFrameBytes() {
            return (sampleRate * frameDurationMs / 1000) * channels * (bitsPerSample / 8);
        }
    }

    public static final AudioSpec STANDARD_AUDIO = new AudioSpec(48000, 1, 16, 20);

    // JSON hello
    public record HelloMessage(String type, String role) {
        public HelloMessage(String role) {
            this("hello", role);
        }
        public String toJson() {
            return GSON.toJson(this);
        }
    }

    // JSON action
    public record ActionMessage(String type, String action) {
        public static final ActionMessage START_DIALOG = new ActionMessage("start_dialog");
        public static final ActionMessage END_DIALOG = new ActionMessage("end_dialog");
        public static final ActionMessage SKIP_DIALOG = new ActionMessage("skip_dialog");

        public ActionMessage(String action) { this("action", action); }
        public String toJson() { return GSON.toJson(this); }
    }

    // JSON state response
    public record StateMessage(String type, String state, String message) {
        public static StateMessage fromJson(String json) {
            return GSON.fromJson(json, StateMessage.class);
        }
    }

    // JSON dialog state event
    public record DialogStateEvent(String type, boolean active, int secondsPassed) {
        public DialogStateEvent(boolean active, int secondsPassed) {
            this("dialog_state", active, secondsPassed);
        }

        public String toJson() {
            return GSON.toJson(this);
        }

        public static DialogStateEvent fromJson(String json) {
            return GSON.fromJson(json, DialogStateEvent.class);
        }
    }


    private BridgeProtocol() {}
}