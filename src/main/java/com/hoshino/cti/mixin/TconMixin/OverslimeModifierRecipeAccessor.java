package com.hoshino.cti.mixin.TconMixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import slimeknights.tconstruct.library.recipe.modifiers.adding.OverslimeModifierRecipe;

@Mixin(value = OverslimeModifierRecipe.class,remap = false)
public interface OverslimeModifierRecipeAccessor {
    @Accessor("restoreAmount")
    int getRestoreAmount();
}
