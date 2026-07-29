package me.markerra.bridge.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.markerra.bridge.AudioBridge;
import me.markerra.voice.VoiceChatManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;


public class BridgeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("bridge")

                        .then(Commands.literal("start")
                                .executes(ctx -> start(ctx.getSource())))

                        .then(Commands.literal("stop")
                                .executes(ctx -> stop(ctx.getSource())))

                        .then(Commands.literal("status")
                                .executes(ctx -> status(ctx.getSource())))

                        .then(Commands.literal("spawn")
                                .executes(ctx -> spawn(ctx.getSource())))

                        .then(Commands.literal("remove")
                                .executes(ctx -> remove(ctx.getSource())))

                        .then(Commands.literal("playvoice")
                                .executes(ctx -> playVoice(ctx.getSource())))

                        .then(Commands.literal("stopvoice")
                                .executes(ctx -> stopVoice(ctx.getSource())))
        );
    }

    private static int start(CommandSourceStack source) {

        AudioBridge.start();

        source.sendSuccess(
                () -> Component.literal("Bridge started."),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int stop(CommandSourceStack source) {

        AudioBridge.stop();

        source.sendSuccess(
                () -> Component.literal("Bridge stopped."),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int status(CommandSourceStack source) {

        source.sendSuccess(
                () -> Component.literal("========== Bridge =========="),
                false
        );

        source.sendSuccess(
                () -> Component.literal(
                        "Voice API: " +
                                (VoiceChatManager.isReady() ? "READY" : "NOT READY")),
                false
        );

        source.sendSuccess(
                () -> Component.literal(
                        "Queued Frames: " +
                                VoiceChatManager.getStreamer().getQueue().size()),
                false
        );

        source.sendSuccess(
                () -> Component.literal(
                        "Playing: " +
                                VoiceChatManager.getAudioPlayer().isPlaying()),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int spawn(CommandSourceStack source) {

        MinecraftServer server = source.getServer();

        ServerLevel level = server.overworld();

        Vec3 pos = source.getPosition();

        boolean success = VoiceChatManager.getNpc().spawn(
                level,
                pos.x,
                pos.y,
                pos.z
        );

        if (success) {

            source.sendSuccess(
                    () -> Component.literal("NPC spawned."),
                    false
            );

        } else {

            source.sendFailure(
                    Component.literal("Failed to spawn NPC.")
            );

        }

        return Command.SINGLE_SUCCESS;
    }

    private static int remove(CommandSourceStack source) {

        VoiceChatManager.getNpc().remove();

        source.sendSuccess(
                () -> Component.literal("NPC removed."),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int playVoice(CommandSourceStack source) {

        if (VoiceChatManager.getNpc().getChannel() == null) {

            source.sendFailure(
                    Component.literal("Spawn NPC first.")
            );

            return 0;
        }

        VoiceChatManager.getAudioPlayer().play(
                VoiceChatManager.getNpc().getChannel()
        );

        source.sendSuccess(
                () -> Component.literal("Audio started."),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int stopVoice(CommandSourceStack source) {

        VoiceChatManager.getAudioPlayer().stop();

        source.sendSuccess(
                () -> Component.literal("Audio stopped."),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

}