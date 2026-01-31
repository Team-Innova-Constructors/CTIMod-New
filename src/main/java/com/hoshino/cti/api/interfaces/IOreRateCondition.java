package com.hoshino.cti.api.interfaces;

import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;

import java.util.function.Supplier;

public interface IOreRateCondition {
    int getRate(IMeltingContainer.OreRateType rate, int amount);

    public static class BiRateCondition implements IOreRateCondition{
        private final Supplier<Boolean> booleanSupplier;
        private final int falseValue;
        private final int trueValue;

        public BiRateCondition(Supplier<Boolean> booleanSupplier, int falseValue, int trueValue) {
            this.booleanSupplier = booleanSupplier;
            this.falseValue = falseValue;
            this.trueValue = trueValue;
        }

        @Override
        public int getRate(IMeltingContainer.OreRateType rate, int amount) {
            return (booleanSupplier.get()?trueValue:falseValue)*amount;
        }
    }
}
