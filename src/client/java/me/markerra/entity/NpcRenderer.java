package me.markerra.entity;

import me.markerra.bridge.AudioBridge;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;

public class NpcRenderer extends HumanoidMobRenderer<NpcEntity, NpcRenderState, HumanoidModel<NpcRenderState>> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("nekto_voice", "textures/entity/npc_skin.png");

    public NpcRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public NpcRenderState createRenderState() {
        return new NpcRenderState();
    }

    @Override
    public Identifier getTextureLocation(NpcRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(NpcEntity entity, NpcRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.isDialogActive = AudioBridge.isDialogActive();
        state.dialogSeconds = AudioBridge.getDialogSeconds();
    }

}