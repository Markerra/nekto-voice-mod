package me.markerra.voice;

import de.maxhenkel.voicechat.api.VoicechatServerApi;
import me.markerra.bridge.audio.NpcVoiceStreamer;
import me.markerra.voice.audio.AudioPlayer;
import me.markerra.voice.audio.NpcAudioSource;

public class VoiceChatManager {
    private static VoicechatServerApi api;

    private static final NpcAudioSource NPC = new NpcAudioSource();

    private static final AudioPlayer AUDIO_PLAYER = new AudioPlayer();

    private static final NpcVoiceStreamer STREAMER = new NpcVoiceStreamer();

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

    public static AudioPlayer getAudioPlayer() {
        return AUDIO_PLAYER;
    }

    public static NpcVoiceStreamer getStreamer() {
        return STREAMER;
    }

    public static NpcAudioSource getNpc() {
        return NPC;
    }

}