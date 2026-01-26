package com.hoshino.cti.Items;

import com.hoshino.cti.util.SearchTools;
import lombok.Getter;

@Getter
public enum SoyoKeyCategory {
    red(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    orange(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    yellow(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    green(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    blue(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    aqua(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    purple(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    pink(new MatrixReward[]{
            new MatrixReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new MatrixReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new MatrixReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    });
    private final MatrixReward[] reward;

    SoyoKeyCategory(MatrixReward[] reward) {
        this.reward = reward;
    }
}
