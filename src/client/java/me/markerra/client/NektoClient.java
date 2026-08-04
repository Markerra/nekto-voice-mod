package me.markerra.client;

import me.markerra.entity.ModEntities;
import me.markerra.entity.NpcRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class NektoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntities.NPC, NpcRenderer::new);
    }
}
