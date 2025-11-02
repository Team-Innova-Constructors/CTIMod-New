package com.hoshino.cti.mixin.TwiMixin;

import com.hoshino.cti.util.AdvanceMentHelper;
import com.hoshino.cti.util.ChangeBossHealth;
import com.hoshino.cti.util.TwiAdvanceID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.entity.spawner.BossSpawnerBlockEntity;


@Mixin(value = BossSpawnerBlockEntity.class)
public abstract class BossSpawnerBlockEntityMixin<T extends Mob> extends BlockEntity {

    @Shadow(remap = false)
    protected abstract T makeMyCreature();

    @Shadow(remap = false)
    protected abstract int getRange();

    public BossSpawnerBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    protected boolean spawnMyBoss(ServerLevelAccessor accessor) {
        var myCreature = this.makeMyCreature();
        myCreature.moveTo(this.getBlockPos().below(), accessor.getLevel().getRandom().nextFloat() * 360.0F, 0.0F);
        myCreature.finalizeSpawn(accessor, accessor.getCurrentDifficultyAt(this.getBlockPos()), MobSpawnType.SPAWNER, null, null);
        var pos = myCreature.getOnPos();
        ResourceLocation[] cti$threeBossAdvancePath = new ResourceLocation[]{
                TwiAdvanceID.KillGlacier.getResourceLocation(),
                TwiAdvanceID.KillHydra.getResourceLocation(),
                TwiAdvanceID.KillUrGhast.getResourceLocation()
        };
        if (this.level != null) {
            var player = this.level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), getRange() + 2, false);
            boolean shouldIncrease = false;
            if (player instanceof ServerPlayer serverPlayer) {
                for (ResourceLocation location : cti$threeBossAdvancePath) {
                    if (AdvanceMentHelper.hasCompletedAdvancement(serverPlayer, location)) {
                        shouldIncrease = true;
                        break;
                    }
                }
                if (shouldIncrease) {
                    var uuid = player.getUUID();
                    if (myCreature instanceof ChangeBossHealth changeBossHealth) {
                        changeBossHealth.cti$changeMaxHealthAttributeInstance(attributeInstance -> {
                            var modifier = new AttributeModifier(uuid, attributeInstance.getAttribute().getDescriptionId(), 1.8f, AttributeModifier.Operation.MULTIPLY_TOTAL);
                            attributeInstance.addPermanentModifier(modifier);
                        });
                    }
                }
            }
        }
        return accessor.addFreshEntity(myCreature);
    }
}
