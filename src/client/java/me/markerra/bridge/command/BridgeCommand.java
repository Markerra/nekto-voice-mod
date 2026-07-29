package me.markerra.bridge.command;

import com.mojang.brigadier.CommandDispatcher;
import me.markerra.bridge.AudioBridgeTest;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class BridgeCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {

        dispatcher.register(
            ClientCommandManager.literal("bridge")
            .then(
                    ClientCommandManager.literal("start")
                            .executes(context -> {
                                AudioBridgeTest.runTest();
                                System.out.println("[Bridge] Start");

                                return 1;
                            })
            )
            .then(
                    ClientCommandManager.literal("stop")
                            .executes(context -> {
                                AudioBridgeTest.stopTest();
                                System.out.println("[Bridge] Stop");

                                return 1;
                            })
            )
        );

    }

}