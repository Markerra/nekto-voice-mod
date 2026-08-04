package me.markerra.voice.audio;

import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
import me.markerra.entity.ModEntities;
import me.markerra.entity.NpcEntity;
import me.markerra.voice.VoiceChatManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;

import java.util.UUID;

public class NpcAudioSource {

    private NpcEntity npc;
    private EntityAudioChannel channel;

    public boolean spawn(ServerLevel level, double x, double y, double z, float rotation) {

        VoicechatServerApi api = VoiceChatManager.getApi();

        if (api == null) {
            System.out.println("[NPC] VoiceChat API not ready");
            return false;
        }

        npc = ModEntities.NPC.create(level, EntitySpawnReason.COMMAND);

        if (npc == null) {
            System.out.println("[NPC] Failed to create entity");
            return false;
        }

        npc.snapTo(
                x,
                y,
                z,
                rotation,
                0F
        );

        level.addFreshEntity(npc);

        channel = api.createEntityAudioChannel(
                UUID.randomUUID(),
                api.fromEntity(npc)
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

        if (npc != null) {
            npc.discard();
            npc = null;
        }

        channel = null;
    }

    public EntityAudioChannel getChannel() {
        return channel;
    }

    public NpcEntity getEntity() {
        return npc;
    }
}