package me.markerra.entity;

import me.markerra.bridge.AudioBridge;
import me.markerra.bridge.BridgeProtocol;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NpcEntity extends PathfinderMob {

    public NpcEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        setPersistenceRequired();
    }

    private boolean shouldFollow = true;

    public boolean shouldFollowPlayer() {
        if (!AudioBridge.isDialogActive()) return false;
        return this.shouldFollow;
    }

    public void setShouldFollow(boolean shouldFollow) {
        this.shouldFollow = shouldFollow;
    }

    public static AttributeSupplier.Builder createAttributes() {

        return PathfinderMob.createMobAttributes()

                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 24D);

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));

        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new FollowPlayerGoal(this, 1.0D, 3.0F, 2.0F));

    }

    @Override
    public void tick() {

        super.tick();

        setDeltaMovement(0D, getDeltaMovement().y, 0D);

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {

        if (source.getEntity() instanceof Player player) {

            System.out.println("[NPC] Player " + player.getName().getString() + " hurt NPC");

            AudioBridge.sendAction(BridgeProtocol.ActionMessage.SKIP_DIALOG);
            boolean hurtResult = super.hurtServer(level, source, amount);

            this.setHealth(this.getMaxHealth());

            return hurtResult;
        }
        else {
            this.discard();
        }
        return true;

    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {

        return InteractionResult.SUCCESS;

    }
}