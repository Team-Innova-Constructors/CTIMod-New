package com.hoshino.cti.mixin.ModularRouterMixin;

import appeng.blockentity.AEBaseBlockEntity;
import me.desht.modularrouters.logic.ModuleTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModuleTarget.class,remap = false)
public class ModuleTargetMixin {
    @Shadow
    private LazyOptional<IItemHandler> cachedItemCap;

    @Shadow
    @Final
    public GlobalPos gPos;

    @Shadow
    @Final
    public Direction face;

    /**
     * @author
     * @reason 修复路由器不更新AE2设备的Cap导致刷物品
     */
    @Overwrite
    private LazyOptional<IItemHandler> getItemHandlerFor(Level w) {
        BlockPos pos = gPos.pos();
        BlockEntity te = w.getBlockEntity(pos);
        if (te instanceof AEBaseBlockEntity)
            return te.getCapability(ForgeCapabilities.ITEM_HANDLER, face);
        if (!cachedItemCap.isPresent()) {
            if (w == null || !w.isLoaded(pos)) {
                cachedItemCap = LazyOptional.empty();
            } else {
                cachedItemCap = te == null ? LazyOptional.empty() : te.getCapability(ForgeCapabilities.ITEM_HANDLER, face);
            }
            if (cachedItemCap.isPresent()) cachedItemCap.addListener(c -> cachedItemCap = LazyOptional.empty());
        }
        return cachedItemCap;
    }
}
