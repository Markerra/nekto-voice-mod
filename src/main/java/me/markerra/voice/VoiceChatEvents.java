package me.markerra.voice;

import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;

public class VoiceChatEvents {

    public static void onServerStarted(VoicechatServerStartedEvent event) {

        VoicechatServerApi api = event.getVoicechat();

        VoiceChatManager.setApi(api);

        System.out.println("[VoiceChat] Server started");

    }

    public static void onServerStopped(VoicechatServerStartedEvent event) {

        System.out.println("[VoiceChat] Server stopped");

    }

}