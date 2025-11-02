package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.RawDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.RestrictedCompoundTag;

import java.util.List;

public class SelfRegulate extends EtSTBaseModifier implements RawDataModifierHook {
    public static final String KEY_LAST_DURABILITY = "self_regulate_durability";
    public static final String KEY_BONUS = "self_regulate_bonus";

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.RAW_DATA);
    }

    @Override
    public void addRawData(IToolStackView tool, ModifierEntry modifier, RestrictedCompoundTag tag) {
        if (tag.getInt(KEY_LAST_DURABILITY)<=0){
            tag.putInt(KEY_LAST_DURABILITY,tool.getStats().getInt(ToolStats.DURABILITY));
        } else {
            int last_durability = tag.getInt(KEY_LAST_DURABILITY);
            int durability = tool.getStats().getInt(ToolStats.DURABILITY);
            if (durability>last_durability) {
                float bonus = tag.getFloat(KEY_BONUS);
                tag.putFloat(KEY_BONUS, (float) (bonus+modifier.getLevel()*Math.log10(durability-last_durability)/Math.log10(1.2)));
                tag.putFloat(KEY_LAST_DURABILITY,durability);
            }
        }
    }

    @Override
    public void removeRawData(IToolStackView tool, Modifier modifier, RestrictedCompoundTag tag) {
        tag.remove(KEY_LAST_DURABILITY);
        tag.remove(KEY_BONUS);
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        ToolStack toolStack = (ToolStack) tool;
        if (toolStack.getRestrictedNBT().getFloat(KEY_BONUS)>0){
            damage+=toolStack.getRestrictedNBT().getFloat(KEY_BONUS);
        }
        return damage;
    }

    @Override
    public void modifierOnProjectileLaunch(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, NamespacedNBT namespacedNBT, boolean primary) {
        ToolStack toolStack = (ToolStack) tool;
        if (toolStack.getRestrictedNBT().getFloat(KEY_BONUS)>0&&abstractArrow!=null){
            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage()+toolStack.getRestrictedNBT().getFloat(KEY_BONUS));
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ToolStack toolStack = (ToolStack) tool;
        if (toolStack.getRestrictedNBT().getFloat(KEY_BONUS)>0) {
            tooltip.add(Component.translatable("cti.tooltip.modifier.self_regulate").append(String.format("%.2f",toolStack.getRestrictedNBT().getFloat(KEY_BONUS))).withStyle(this.getDisplayName().getStyle()));
        }
    }
}
