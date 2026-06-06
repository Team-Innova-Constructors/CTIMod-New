package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.entity.AetherBossMob;
import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import com.hoshino.cti.util.mixin.slider.ISliderMixin;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = Slider.class,remap = false)
public abstract class SliderMixin extends PathfinderMob implements AetherBossMob<Slider>, Enemy, IEntityAdditionalSpawnData, ISliderMixin {
    protected SliderMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Shadow
    public abstract boolean isAwake();

    @Shadow
    protected abstract Optional<LivingEntity> sendInvalidToolMessage(LivingEntity attacker);

    @Shadow
    protected abstract Optional<LivingEntity> sendTooFarMessage(LivingEntity attacker);

    /*
    @Unique private static final EntityDataAccessor<Boolean> cti$DATA_CRITICAL_ID = SynchedEntityData.defineId(Slider.class,EntityDataSerializers.BOOLEAN);

    @Inject(at = @At("HEAD"),method = "defineSynchedData")
    private void addExtraData(CallbackInfo ci){
        this.entityData.define(cti$DATA_CRITICAL_ID,false);
    }

    @Inject(at = @At("RETURN"),method = "isCritical",cancellable = true)
    private void modifyCritical(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(this.entityData.get(cti$DATA_CRITICAL_ID));
    }

    @Inject(at = @At("HEAD"),method = "canDamageSlider",cancellable = true)
    private void canDamageSlider(DamageSource source, CallbackInfoReturnable<Optional<LivingEntity>> cir) {
        if (this.getLevel().getDifficulty() != Difficulty.PEACEFUL) {
            if (source.getDirectEntity() instanceof LivingEntity attacker) {
                if (this.getDungeon() == null || this.getDungeon().isPlayerWithinRoomInterior(attacker)) {
                    if (this.isCritical())
                        cir.setReturnValue(Optional.of(attacker));
                    else if (attacker.getMainHandItem().canPerformAction(ToolActions.PICKAXE_DIG)
                            || attacker.getMainHandItem().is(AetherTags.Items.SLIDER_DAMAGING_ITEMS)
                            || attacker.getMainHandItem().isCorrectToolForDrops(AetherBlocks.CARVED_STONE.get().defaultBlockState())) {
                        cir.setReturnValue(Optional.of(attacker));
                    } else {
                        cir.setReturnValue(sendInvalidToolMessage(attacker));
                    }
                } else {
                    sendTooFarMessage(attacker);
                }
            }
            cir.setReturnValue(Optional.empty());
        }
    }
     */

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isCritical() {
        return this.getHealth() <= (this.getMaxHealth() * 0.25F);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getVelocityIncrease() {
        float healthRatio = this.getHealth() / this.getMaxHealth();
        if (healthRatio > 1.0F) healthRatio = 1.0F;
        if (healthRatio < 0.0F) healthRatio = 0.0F;
        if (this.isCritical()) {
            return 0.045F - (healthRatio * 0.04F);
        } else {
            return 0.035F - (healthRatio * 0.0133F);
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public float getMaxVelocity() {
        float healthRatio = this.getHealth() / this.getMaxHealth();
        return 3.5F - healthRatio;
    }


}
