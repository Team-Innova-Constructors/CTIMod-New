package com.hoshino.cti.mixin.IAFMixin;

import com.github.alexthe666.iceandfire.entity.props.ChainProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(ChainProperties.class)
public class ChainPropertiesMixin {
    @Shadow(remap = false)
    private static HashMap<CompoundTag, Boolean> containsChainData;

    /**
     * @author EtSH_C2H6S
     * @reason 修复IAF获取列表元素时空指针
     */
    @Overwrite(remap = false)
    private static ListTag getOrCreateChainData(CompoundTag entityData) {
        if (containsChainData.containsKey(entityData) && containsChainData.getOrDefault(entityData,false) && entityData.contains("ChainDataIaf", 9)) {
            return entityData.getList("ChainDataIaf", 10);
        } else if (entityData.contains("ChainDataIaf", 9)) {
            containsChainData.put(entityData, true);
            return entityData.getList("ChainDataIaf", 10);
        } else {
            containsChainData.put(entityData, false);
            return new ListTag();
        }
    }
}
