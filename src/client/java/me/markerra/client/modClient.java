package me.markerra.client;

import me.markerra.bridge.AudioBridgeTest;
import net.fabricmc.api.ClientModInitializer;

public class modClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AudioBridgeTest.runTest();
    }
}
