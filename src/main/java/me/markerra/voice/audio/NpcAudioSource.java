package me.markerra.voice.audio;

import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import me.markerra.voice.VoiceChatManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.UUID;

public class NpcAudioSource {

    private ArmorStand armorStand;
    private EntityAudioChannel channel;

    public boolean spawn(ServerLevel level, double x, double y, double z) {

        VoicechatServerApi api = VoiceChatManager.getApi();

        if (api == null) {
            System.out.println("[NPC] VoiceChat API not ready");
            return false;
        }

        armorStand = new ArmorStand(
                level,
                x,
                y,
                z
        );

        armorStand.setNoGravity(true);
        armorStand.setInvisible(true);

        level.addFreshEntity(armorStand);

        channel = api.createEntityAudioChannel(
                UUID.randomUUID(),
                api.fromEntity(armorStand)
        );

        if (channel == null) {
            System.out.println("[NPC] Failed to create EntityAudioChannel");
            return false;
        }

        channel.setDistance(24F);

        System.out.println("[NPC] Audio source created");

        return true;
    }

    public void remove() {

        VoiceChatManager.getAudioPlayer().stop();

        if (armorStand != null) {
            armorStand.discard();
            armorStand = null;
        }

        channel = null;
    }

    public EntityAudioChannel getChannel() {
        return channel;
    }

}