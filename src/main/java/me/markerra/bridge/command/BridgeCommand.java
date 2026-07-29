package me.markerra.bridge.command;

import com.mojang.brigadier.CommandDispatcher;
import me.markerra.bridge.AudioBridgeTest;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;


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
        );
    }
}