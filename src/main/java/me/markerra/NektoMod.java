package me.markerra;

import me.markerra.bridge.AudioBridge;
import me.markerra.bridge.command.BridgeCommand;
import me.markerra.entity.ModEntities;
import me.markerra.entity.NpcEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class NektoMod implements ModInitializer {
    public static final String MOD_ID = "nekto_voice";

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BridgeCommand.register(dispatcher);
        });

        ModEntities.registerModEntities();

        FabricDefaultAttributeRegistry.register(
                ModEntities.NPC,
                NpcEntity.createAttributes()
        );

        AudioBridge.start();
    }
}
