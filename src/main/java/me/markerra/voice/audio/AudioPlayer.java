package me.markerra.voice.audio;

import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import me.markerra.bridge.audio.NpcVoiceStreamer;
import me.markerra.voice.VoiceChatManager;

public class AudioPlayer {

    private de.maxhenkel.voicechat.api.audiochannel.AudioPlayer player;
    private EntityAudioChannel channel;
    private NpcVoiceStreamer streamer;

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void stop() {

        if (player != null) {
            player.stopPlaying();
            player = null;
        }

        channel = null;
    }

    public void play(EntityAudioChannel entityChannel) {

        stop();

        VoicechatServerApi api = VoiceChatManager.getApi();

        if (api == null) {
            System.out.println("[VoiceChat] API not ready");
            return;
        }

        channel = entityChannel;

        OpusEncoder encoder = api.createEncoder();

        if (encoder == null) {
            System.out.println("[VoiceChat] Failed to create Opus encoder");
            return;
        }

        streamer = VoiceChatManager.getStreamer();

        player = api.createAudioPlayer(
                channel,
                encoder,
                new PcmAudioSupplier(streamer.getQueue())
        );

        player.startPlaying();


    }

}