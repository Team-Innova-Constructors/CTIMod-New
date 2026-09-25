package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.Blocks.BlockEntity.tinker.refinery.RefineryControllerBlockEntity;
import com.hoshino.cti.util.ICtiMeltingModule;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule;

import javax.annotation.Nullable;

import static slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule.NO_SPACE;

@Mixin(value = MeltingModule.class,remap = false)
public abstract class MeltingModuleMixin implements ICtiMeltingModule {

    @Shadow private int currentTime;

    @Shadow public abstract boolean canHeatItem(int temperature);

    @Shadow private int requiredTime;

    @Shadow protected abstract boolean onItemFinishedHeating();

    @Shadow protected abstract void resetRecipe();

    @Shadow
    @Final
    private MantleBlockEntity parent;

    @Shadow
    @Nullable
    protected abstract IMeltingRecipe findRecipe();

    @Shadow
    private ItemStack stack;

    @Override
    public void cti$heatItem(int temperature,float speedMod) {
        if (this.currentTime == NO_SPACE || this.canHeatItem(temperature)) {
            // if we are done, cook item
            if (currentTime == NO_SPACE || currentTime >= this.requiredTime) {
                if (this.onItemFinishedHeating()) {
                    this.resetRecipe();
                }
            } else {
                currentTime += (int) (temperature * speedMod / 100);
            }
        }
    }

    @Redirect(method = "setStack",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/smeltery/block/entity/module/MeltingModule;findRecipe()Lslimeknights/tconstruct/library/recipe/melting/IMeltingRecipe;"))
    private IMeltingRecipe redirectIfRefinery(MeltingModule module){
        if (this.parent instanceof RefineryControllerBlockEntity refinery&&refinery.getLevel()!=null){
            var item = this.stack.getItem();
            var cache = refinery.cacheMap.get(item);
            if (cache!=null){
                refinery.cacheMap.put(item,new RefineryControllerBlockEntity.RefineryRecipeCache(cache.recipe(),refinery.getLevel().getGameTime()));
                return cache.recipe();
            }
        }
        var recipe = this.findRecipe();
        if (this.parent instanceof RefineryControllerBlockEntity refinery&&refinery.getLevel()!=null){
            var stack = this.stack;
            if (!stack.isEmpty())
                refinery.cacheMap.put(stack.getItem(),new RefineryControllerBlockEntity.RefineryRecipeCache(recipe,refinery.getLevel().getGameTime()));
        }
        return recipe;
    }
}
