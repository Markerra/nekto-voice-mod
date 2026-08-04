package me.markerra.entity;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class FollowPlayerGoal extends Goal {
    private final NpcEntity mob;
    private Player targetPlayer;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;

    public FollowPlayerGoal(NpcEntity mob, double speedModifier, float startDistance, float stopDistance) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.shouldFollowPlayer()) return false;

        double maxSearchRange = this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE);
        this.targetPlayer = this.mob.level().getNearestPlayer(this.mob, maxSearchRange);

        if (this.targetPlayer == null) {
            return false;
        }

        if (this.targetPlayer.isSpectator() || this.targetPlayer.isCreative()) {
            return false;
        }

        // Начинаем идти, только если игрок ДАЛЬШЕ чем startDistance
        return this.mob.distanceToSqr(this.targetPlayer) > (double) (this.startDistance * this.startDistance);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.mob.shouldFollowPlayer()) return false;
        
        if (this.targetPlayer == null || !this.targetPlayer.isAlive() || this.targetPlayer.isSpectator() || this.targetPlayer.isCreative()) {
            return false;
        }

        // Продолжаем идти, пока не подойдем БЛИЖЕ чем stopDistance
        double distSqr = this.mob.distanceToSqr(this.targetPlayer);
        return distSqr > (double) (this.stopDistance * this.stopDistance);
    }

    @Override
    public void start() {
        // Заставляем навигатор сразу проложить путь
        if (this.targetPlayer != null) {
            this.mob.getNavigation().moveTo(this.targetPlayer, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetPlayer != null) {
            this.mob.getLookControl().setLookAt(this.targetPlayer, 10.0F, (float) this.mob.getMaxHeadXRot());

            // Обновляем маршрут каждые 10 тиков (0.5 сек), чтобы не нагружать процессор
            if (this.mob.tickCount % 10 == 0) {
                this.mob.getNavigation().moveTo(this.targetPlayer, this.speedModifier);
            }
        }
    }
}