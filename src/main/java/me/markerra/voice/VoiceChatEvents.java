package me.markerra.voice;

import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.Position;

import me.markerra.bridge.AudioBridge;
import me.markerra.entity.NpcEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;

public class VoiceChatEvents {
    public static void onServerStarted(VoicechatServerStartedEvent event) {
        VoicechatServerApi api = event.getVoicechat();

        VoiceChatManager.setApi(api);

        System.out.println("[VoiceChat] Server started");
    }

    public static void onServerStopped(VoicechatServerStartedEvent event) {
        System.out.println("[VoiceChat] Server stopped");
    }

    public static void onMicrophonePacket(MicrophonePacketEvent event, Map<UUID, OpusDecoder> decoders) {
        if (!VoiceChatManager.isReady()) return;

        var connection = event.getSenderConnection();
        if (connection == null) return;

        UUID playerUuid = connection.getPlayer().getUuid();

        // 1. get player and npc coordinates
        Position playerPos = connection.getPlayer().getPosition();
        NpcEntity npc = VoiceChatManager.getNpc().getEntity();

        if (npc == null) return;

        // 2. check is player crouching
        Player mcPlayer = npc.level().getPlayerByUUID(playerUuid);
        if (mcPlayer != null && mcPlayer.isCrouching()) {
            return;
        }

        // 3. check the distance
        double distance = Math.sqrt(
                Math.pow(playerPos.getX() - npc.getX(), 2) +
                        Math.pow(playerPos.getY() - npc.getY(), 2) +
                        Math.pow(playerPos.getZ() - npc.getZ(), 2)
        );

        double maxDistance = 24.0;
        if (distance > maxDistance) {
            return; // player too far
        }

        // 3. decode OPUS into PCM
        byte[] opusData = event.getPacket().getOpusEncodedData();
        if (opusData == null || opusData.length == 0) return;

        VoicechatServerApi api = VoiceChatManager.getApi();
        OpusDecoder decoder = decoders.computeIfAbsent(playerUuid, id -> api.createDecoder());

        // Decode returns short array (16-bit PCM audio)
        short[] decodedAudio = decoder.decode(opusData);

        // 4. apply volume multiplier
        float volumeMultiplier = (float) (1.0 - (distance / maxDistance));

        // clamp
        volumeMultiplier = Math.max(0.0f, Math.min(1.5f, volumeMultiplier));

        for (int i = 0; i < decodedAudio.length; i++) {
            decodedAudio[i] = (short) (decodedAudio[i] * volumeMultiplier);
        }

        // 5. convert short into bytes (Little Endian)
        byte[] pcmBytes = convertShortArrayToByteArray(decodedAudio);

        // 6. send to websocket
        AudioBridge.sendBinaryAudio(pcmBytes);
    }

    // Утилита для конвертации массива short в массив байтов
    private static byte[] convertShortArrayToByteArray(short[] shortArray) {
        byte[] byteArray = new byte[shortArray.length * 2];
        for (int i = 0; i < shortArray.length; i++) {
            byteArray[i * 2] = (byte) (shortArray[i] & 0xFF);
            byteArray[i * 2 + 1] = (byte) ((shortArray[i] >> 8) & 0xFF);
        }
        return byteArray;
    }
}