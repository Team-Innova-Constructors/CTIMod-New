package com.hoshino.cti.mixin.CMMixin;

import com.hoshino.cti.Blocks.BlockEntity.Ports.BasicPort;
import com.hoshino.cti.util.IMixinMachineTile;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.common.util.BlockStructure;
import fr.frinn.custommachinery.common.util.PartialBlockState;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(value = BlockStructure.class,remap = false)
public abstract class BlockStructureMixin {
    @Shadow
    public abstract Map<BlockPos, IIngredient<PartialBlockState>> getBlocks(Direction direction);

    /**
     * @author EtSH_C2H6S
     * @reason 让多方快检测时将机器位置添加到接口里
     */
    @Overwrite
    public boolean match(LevelReader world, BlockPos machinePos, Direction machineFacing) {
        Map<BlockPos, IIngredient<PartialBlockState>> blocks = getBlocks(machineFacing);
        BlockPos.MutableBlockPos worldPos = new BlockPos.MutableBlockPos();
        for(BlockPos pos : blocks.keySet()) {
            IIngredient<PartialBlockState> ingredient = blocks.get(pos);
            worldPos.set(pos.getX() + machinePos.getX(), pos.getY() + machinePos.getY(), pos.getZ() + machinePos.getZ());
            BlockInWorld info = new BlockInWorld(world, worldPos, false);
            if(ingredient.getAll().stream().noneMatch(state -> state.test(info))){
                return false;
            }
            BlockEntity port = world.getBlockEntity(worldPos);
            if (pos!=machinePos&&port instanceof BasicPort basicPort) {
                basicPort.controllerPos = machinePos;
            }
        }
        return true;
    }
}
