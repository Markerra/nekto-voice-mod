package me.markerra.voice;

import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BridgeVoicechatPlugin implements VoicechatPlugin {
    private final Map<UUID, OpusDecoder> decoders = new ConcurrentHashMap<>();

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

        registration.registerEvent(MicrophonePacketEvent.class,
                (event) -> VoiceChatEvents.onMicrophonePacket(event, decoders));
    }

}