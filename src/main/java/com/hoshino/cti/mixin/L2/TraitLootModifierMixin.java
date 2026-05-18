package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import slimeknights.tconstruct.library.modifiers.ModifierId;

@Mixin(TraitLootModifier.class)
public abstract class TraitLootModifierMixin extends LootModifier {
    protected TraitLootModifierMixin(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }
    @ModifyVariable(
            method = "doApply",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;m_41613_()I"),
            name = "factor",remap = false)
    private double injectExtraWeight(double factor, ObjectArrayList<ItemStack> list, LootContext context) {
        var entity = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if(entity instanceof Player player){
            if(GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())){
                double myWeight = 4.0;
                return factor * myWeight;
            }
        }
        return factor;
    }
    @ModifyVariable(
            method = "doApply",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;m_41613_()I"),
            name = "rate",remap = false)
    private double injectExtraRate(double rate, ObjectArrayList<ItemStack> list, LootContext context) {
        var entity = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if(entity instanceof Player player){
            if(GetModifierLevel.EquipHasModifierlevel(player,new ModifierId("solidarytinker:littlecat"))){
                double myRate = 2.5f;
                return rate * myRate;
            }
        }
        return rate;
    }
}
