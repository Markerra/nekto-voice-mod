package me.markerra.client;

import me.markerra.bridge.command.BridgeCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class modClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess) ->
                BridgeCommand.register(dispatcher)
        );
    }
}
