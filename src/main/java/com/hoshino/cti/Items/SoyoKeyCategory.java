package com.hoshino.cti.Items;

import com.hoshino.cti.util.SearchTools;
import lombok.Getter;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WritableBookItem;

@Getter
public enum SoyoKeyCategory {
    red(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"),1,0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"),6,33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"),6,12),
    });
    private final MatrixReward[] reward;
    SoyoKeyCategory(MatrixReward[] reward){
        this.reward = reward;
    }
}
