package com.hoshino.cti.Modifier.Replace;

import com.gjhi.tinkersinnovation.register.TinkersInnovationModifiers;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiSlots;
import com.james.tinkerscalibration.Utils;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import com.xiaoyue.tinkers_ingenuity.register.TIModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.*;
import tcintegrations.items.TCIntegrationsModifiers;

import java.util.List;

public class DeepOceanEcho extends BattleModifier {
    private int SeaLevel(ItemStack tool) {
        return ModifierUtil.getModifierLevel(tool, TIModifiers.SEA_DREAM.getId()) + ModifierUtil.getModifierLevel(tool, TCIntegrationsModifiers.WATER_POWERED_MODIFIER.getId())
                + ModifierUtil.getModifierLevel(tool, TIModifiers.DIVISION.getId()) + ModifierUtil.getModifierLevel(tool, TinkersInnovationModifiers.poseidite_weapon.getId()) + ModifierUtil.getModifierLevel(tool, Utils.hydrophilous.getId());
    }

    private int DeepAndDeepLevel(ItemStack tool) {
        return ModifierUtil.getModifierLevel(tool, CtiModifiers.DEEP_AND_DEEP_STATIC_MODIFIER.getId());
    }

    private float EchoAmount(Player player) {
        return Math.max(player.getMaxHealth() * 0.2f, 1) * Math.max(player.getArmorValue() * 0.3f, 1) * Math.max(player.totalExperience * 0.0001f, 1);
    }

    private float DamageAddAmount(Player player, ItemStack tool, int ModifierLevel) {
        return (this.EchoAmount(player)) * 0.1f * ModifierLevel * ((this.SeaLevel(tool) + 1) * 2F) * (this.DeepAndDeepLevel(tool) + 1);
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (attacker instanceof Player player) {
            ItemStack Stack = player.getMainHandItem();
            float TrueDamageBoost = this.DamageAddAmount(player, Stack, level);
            if (livingTarget instanceof Player) {
                return damage * 0f;
            } else return damage + TrueDamageBoost;
        }
        return damage;
    }

    @Override
    public void arrowhurt(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, EntityHitResult hit, AbstractArrow arrow, LivingEntity attacker, LivingEntity target) {
        if (attacker instanceof Player player) {
            ItemStack Stack = player.getMainHandItem();
            float TrueDamageBoost = this.DamageAddAmount(player, Stack, level);
            if (target instanceof Player) {
                arrow.setBaseDamage(0);
            } else arrow.setBaseDamage(arrow.getBaseDamage() + TrueDamageBoost);
        }
    }

    @Override
    public void addVolatileData(IToolContext iToolContext, @NotNull ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        modDataNBT.addSlots(CtiSlots.OCEAN, 5);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            int level = modifier.getLevel();
            list.add(DynamicComponentUtil.BreathColorfulText.getColorfulText("当前回声点数",String.valueOf(this.EchoAmount(player)),new int[] {0x99b1ff},60,1500,false));
            list.add(DynamicComponentUtil.BreathColorfulText.getColorfulText("额外生效的海洋系词条等级",String.valueOf(this.SeaLevel(stack)),new int[] {0x99b1ff},60,1500,false));
            list.add(DynamicComponentUtil.scrollColorfulText.getColorfulText("每点回声所增幅的伤害", 0.1f * level * ((this.SeaLevel(stack) + 1) * 2F) * (this.DeepAndDeepLevel(stack) + 1) + "攻击力",new int[] {0x99b1ff,0xf8c0ff},40,20,false));
            list.add(DynamicComponentUtil.scrollColorfulText.getColorfulText("实际提升的总伤害", this.DamageAddAmount(player, stack, level) + "攻击力",new int[] {0x99b1ff,0xf8c0ff},40,20,false));
        }
    }
}

