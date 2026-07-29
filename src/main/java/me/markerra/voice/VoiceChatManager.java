package me.markerra.voice;

import de.maxhenkel.voicechat.api.VoicechatServerApi;
import me.markerra.voice.audio.AudioTestPlayer;
import me.markerra.voice.audio.NpcAudioSource;

public class VoiceChatManager {
    private static VoicechatServerApi api;

    private static final NpcAudioSource NPC = new NpcAudioSource();

    private static final AudioTestPlayer TEST_PLAYER = new AudioTestPlayer();

    public static void setApi(VoicechatServerApi serverApi) {
        api = serverApi;
        System.out.println("[VoiceChat] Server API initialized");
    }

    public static VoicechatServerApi getApi() {
        return api;
    }

    public static boolean isReady() {
        return api != null;
    }

    public static AudioTestPlayer getTestPlayer() {
        return TEST_PLAYER;
    }

    public static NpcAudioSource getNpc() {
        return NPC;
    }

}