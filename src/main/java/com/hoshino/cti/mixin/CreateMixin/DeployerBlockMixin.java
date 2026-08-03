package com.hoshino.cti.mixin.CreateMixin;

import com.hoshino.cti.util.mixin.create.IDeployerBlockEntityMixin;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.deployer.DeployerBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;
import java.util.function.Consumer;

@Mixin(value = DeployerBlock.class,remap = false)
public class DeployerBlockMixin {
    @Unique
    private Player cti$cachedPlayer;
    @Unique
    private InteractionHand cti$cachedHand;
    @Inject(method = "m_6227_",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/deployer/DeployerBlock;withBlockEntityDo(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Ljava/util/function/Consumer;)V",shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILHARD)
    private void onItemTransfer(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir, ItemStack heldByPlayer, IPlacementHelper placementHelper, Vec3 normal, Vec3 location){
        cti$cachedHand = handIn;
        cti$cachedPlayer = player;
    }

    @ModifyArg(method = "m_6227_",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/deployer/DeployerBlock;withBlockEntityDo(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Ljava/util/function/Consumer;)V"),index = 2)
    private Consumer<DeployerBlockEntity> delayItemTransfer(Consumer<DeployerBlockEntity> par3){
        return be->{
            if (be.getPlayer()!=null&&cti$cachedPlayer!=null&&cti$cachedHand!=null) {
                ItemStack heldItem = cti$cachedPlayer.getItemInHand(cti$cachedHand);
                boolean wasEmptyHanded = heldItem.isEmpty();

                ItemStack mainItemStack = ((IDeployerBlockEntityMixin)be).cti$getHeldItem();
                if (!mainItemStack.isEmpty()) {
                    cti$cachedPlayer.getInventory().placeItemBackInInventory(mainItemStack);
                    ((IDeployerBlockEntityMixin)be).cti$setHeldItem(ItemStack.EMPTY);
                    if (be.getLevel() != null) {
                        be.getLevel().playSound(null, be.getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
                                1f + Create.RANDOM.nextFloat());
                    }
                }
                if (!wasEmptyHanded) {
                    ((IDeployerBlockEntityMixin)be).cti$setHeldItem(heldItem);
                    cti$cachedPlayer.setItemInHand(cti$cachedHand, ItemStack.EMPTY);
                    AllSoundEvents.DEPOT_SLIDE.playOnServer(be.getLevel(), be.getBlockPos());
                }
            }
            be.notifyUpdate();
            cti$cachedHand = null;
            cti$cachedPlayer = null;
        };
    }
}
