package me.markerra.voice;

import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;

public class BridgeVoicechatPlugin implements VoicechatPlugin {

    @Override
    public String getPluginId() {
        return "nektomod";
    }

    @Override
    public void initialize(VoicechatApi api) {
        System.out.println("[VoiceChat] Plugin initialized");
    }

    @Override
    public void registerEvents(EventRegistration registration) {

        registration.registerEvent(VoicechatServerStartedEvent.class,
                VoiceChatEvents::onServerStarted);

        registration.registerEvent(VoicechatServerStartedEvent.class,
                VoiceChatEvents::onServerStopped);
    }

}