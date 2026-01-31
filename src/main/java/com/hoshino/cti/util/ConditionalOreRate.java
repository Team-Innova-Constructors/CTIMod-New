package com.hoshino.cti.util;

import com.hoshino.cti.api.interfaces.IOreRateCondition;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;

public class ConditionalOreRate implements IMeltingContainer.IOreRate {
    protected final IOreRateCondition condition;

    public ConditionalOreRate(IOreRateCondition condition) {
        this.condition = condition;
    }

    @Override
    public int applyOreBoost(IMeltingContainer.OreRateType rate, int amount) {
        if (rate== IMeltingContainer.OreRateType.DEFAULT||rate== IMeltingContainer.OreRateType.NONE) return amount;
        return condition.getRate(rate,amount);
    }
}
