package com.hoshino.cti.mixin.CreateMixin;

import com.hoshino.cti.util.mixin.create.IDeployerBlockEntityMixin;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DeployerBlockEntity.class,remap = false)
public abstract class DeployerBlockEntityMixin extends KineticBlockEntity implements IDeployerBlockEntityMixin {
    @Shadow
    protected ItemStack heldItem;

    @Shadow
    protected DeployerFakePlayer player;

    public DeployerBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void cti$setHeldItem(ItemStack stack) {
        this.heldItem = stack;
        this.player.setItemInHand(InteractionHand.MAIN_HAND,stack);
    }

    @Override
    public ItemStack cti$getHeldItem() {
        return this.heldItem;
    }

    @ModifyArg(method = "write",at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;m_128365_(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;",ordinal = 1),index = 1)
    private Tag storeItemHeld(Tag par2){
        return heldItem.serializeNBT();
    }

    @Redirect(method = "write",at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;m_128365_(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;",ordinal = 4))
    private Tag avoidWritingItemOnClientSide(CompoundTag instance, String s, Tag tag){
        return tag;
    }
}
