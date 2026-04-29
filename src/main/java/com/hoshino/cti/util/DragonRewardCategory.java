package com.hoshino.cti.util;

import com.hoshino.cti.Items.RandomReward;
import lombok.Getter;

public enum DragonRewardCategory {
    lightning(new RandomReward[]{
            new RandomReward(SearchTools.findItem("iceandfire:lightning_dragon_flesh"), 7, 16),
            new RandomReward(SearchTools.findItem("iceandfire:lightning_dragon_heart"), 1, 2),
            new RandomReward(SearchTools.findItem("iceandfire:dragonscales_amythest"), 14, 20),
    }),
    fire(new RandomReward[]{
            new RandomReward(SearchTools.findItem("iceandfire:fire_dragon_flesh"), 7, 16),
            new RandomReward(SearchTools.findItem("iceandfire:ice_dragon_heart"), 1, 2),
            new RandomReward(SearchTools.findItem("iceandfire:dragonscales_red"), 14, 20),
    }),
    ice(new RandomReward[]{
            new RandomReward(SearchTools.findItem("iceandfire:ice_dragon_flesh"), 7, 16),
            new RandomReward(SearchTools.findItem("iceandfire:ice_dragon_heart"), 1, 2),
            new RandomReward(SearchTools.findItem("iceandfire:dragonscales_blue"), 14, 20),
    });
    @Getter
    private final RandomReward[] reward;

    DragonRewardCategory(RandomReward[] reward) {
        this.reward = reward;
    }
}
