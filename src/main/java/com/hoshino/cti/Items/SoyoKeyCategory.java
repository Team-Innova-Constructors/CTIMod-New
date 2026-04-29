package com.hoshino.cti.Items;

import com.hoshino.cti.util.SearchTools;
import lombok.Getter;

@Getter
public enum SoyoKeyCategory {
    red(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    orange(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    yellow(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    green(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    blue(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    aqua(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    purple(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    }),
    pink(new RandomReward[]{
            new RandomReward(SearchTools.findItem("mekanism:ultimate_compressing_factory"), 1, 0),
            new RandomReward(SearchTools.findItem("thermal:steel_ingot"), 6, 33),
            new RandomReward(SearchTools.findItem("thermal:rose_gold_block"), 6, 12),
    });
    private final RandomReward[] reward;

    SoyoKeyCategory(RandomReward[] reward) {
        this.reward = reward;
    }
}
