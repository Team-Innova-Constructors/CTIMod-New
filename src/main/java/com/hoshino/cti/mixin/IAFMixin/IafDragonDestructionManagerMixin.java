package com.hoshino.cti.mixin.IAFMixin;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafDragonDestructionManager;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(value = IafDragonDestructionManager.class,remap = false)
public class IafDragonDestructionManagerMixin {
    @Unique private static Level cti$level = null;
    @Unique private static EntityDragonBase cti$dragon = null;
    @Unique private static boolean cti$canBreakBlocks = false;
    @Shadow private static void attackBlock(Level level, EntityDragonBase dragon, BlockPos position) {}

    @Inject(method = "destroyAreaBreath",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;getMobGriefingEvent(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;)Z",shift = At.Shift.BY,by = 2),locals = LocalCapture.CAPTURE_FAILHARD)
    private static void fetchArgs(Level level, BlockPos center, EntityDragonBase dragon, CallbackInfo ci, int statusDuration, float damageScale, double damageRadius, boolean canBreakBlocks){
        cti$level=level;
        cti$dragon = dragon;
        cti$canBreakBlocks = canBreakBlocks;
    }
    @Inject(method = "destroyAreaBreath",at = @At("TAIL"))
    private static void discardArgs(Level level, BlockPos center, EntityDragonBase dragon, CallbackInfo ci){
        cti$level=null;
        cti$dragon = null;
        cti$canBreakBlocks = false;
    }
    @ModifyArg(method = "destroyAreaBreath",at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V",ordinal = 0))
    private static Consumer<BlockPos> rejectFuelingForge(Consumer<BlockPos> action){
        return position ->{
            if (cti$level!=null&&cti$dragon!=null) {
                if (cti$level.getBlockEntity(position) instanceof TileEntityDragonforgeInput forge&&((TamableAnimal)cti$dragon).isTame()) {
                    forge.onHitWithFlame();
                    return;
                }

                if (cti$canBreakBlocks && DragonUtils.canGrief(cti$dragon) && cti$level.getRandom().nextBoolean()) {
                    attackBlock(cti$level, cti$dragon, position);
                }
            }
        };
    }
    @ModifyArg(method = "destroyAreaBreath",at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V",ordinal = 1))
    private static Consumer<BlockPos> rejectFuelingForge2(Consumer<BlockPos> action){
        return position ->{
            if (cti$level!=null&&cti$dragon!=null) {
                if (cti$level.getBlockEntity(position) instanceof TileEntityDragonforgeInput forge&&((TamableAnimal)cti$dragon).isTame()) {
                    forge.onHitWithFlame();
                    return;
                }

                if (cti$canBreakBlocks && DragonUtils.canGrief(cti$dragon) && cti$level.getRandom().nextBoolean()) {
                    attackBlock(cti$level, cti$dragon, position);
                }
            }
        };
    }
}
