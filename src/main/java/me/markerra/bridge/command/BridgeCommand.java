package me.markerra.bridge.command;

import com.mojang.brigadier.CommandDispatcher;
import me.markerra.bridge.AudioBridgeTest;
import me.markerra.voice.VoiceChatManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;


public class BridgeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("bridge")
                        .then(
                                Commands.literal("start")
                                        .executes(context -> {
                                            AudioBridgeTest.runTest();

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("Bridge Started"),
                                                    false
                                            );

                                            return 1;
                                        })
                        )
                        .then(
                                Commands.literal("stop")
                                        .executes(context -> {
                                            AudioBridgeTest.stopTest();

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("Bridge stopped"),
                                                    false
                                            );

                                            return 1;
                                        })
                        )
                        .then(
                                Commands.literal("test")
                                        .executes(context -> {
                                            MinecraftServer server = context.getSource().getServer();
                                            ServerLevel level = server.overworld();

                                            VoiceChatManager.getNpc().spawn(
                                                    level,
                                                    0,
                                                    -60,
                                                    0
                                            );

                                            VoiceChatManager.getTestPlayer().play(
                                                    VoiceChatManager.getNpc().getChannel()
                                            );

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("NPC Spawned"),
                                                    false
                                            );

                                            return 1;
                                        })
                        )
        );
    }
}