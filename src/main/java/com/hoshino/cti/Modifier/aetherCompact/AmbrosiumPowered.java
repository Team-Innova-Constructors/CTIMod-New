package com.hoshino.cti.Modifier.aetherCompact;

import com.aetherteam.aether.item.AetherItems;
import com.hoshino.cti.Cti;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.SlotStackModifierHook;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class AmbrosiumPowered extends Modifier implements SlotStackModifierHook, ToolStatsModifierHook, TooltipModifierHook, ToolDamageModifierHook , ProjectileLaunchModifierHook, MeleeDamageModifierHook, ModifyDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, CtiModifierHook.SLOT_STACK,ModifierHooks.TOOLTIP,ModifierHooks.TOOL_DAMAGE,ModifierHooks.MELEE_DAMAGE,ModifierHooks.PROJECTILE_LAUNCH,ModifierHooks.MODIFY_DAMAGE);
    }

    public static final ResourceLocation KEY_AMBROSIUM_POWER = Cti.getResource("ambrosium_power");

    @Override
    public boolean overrideOtherStackedOnMe(IToolStackView slotTool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
        if (held.is(AetherItems.AMBROSIUM_SHARD.get())&&slot.allowModification(player)){
            if (!player.level.isClientSide&&slotTool.getPersistentData().getInt(KEY_AMBROSIUM_POWER)<getMaxCharge(modifier)){
                held.shrink(1);
                chargeTool(slotTool);
            }
            return true;
        }
        return false;
    }

    public static int getMaxCharge(ModifierEntry thisModifier){
        return thisModifier.getLevel()*32;
    }
    public static int getMaxCharge(IToolStackView tool){
        return getMaxCharge(tool.getModifier(CtiModifiers.AMBROSIUM_POWERED.getId()));
    }



    public static int chargeTool(IToolStackView toolStackView,int amount){
        if (toolStackView.getModifierLevel(CtiModifiers.AMBROSIUM_POWERED.getId())<=0) return 0;
        var maxCharge = getMaxCharge(toolStackView);
        var power = toolStackView.getPersistentData().getInt(KEY_AMBROSIUM_POWER);
        amount = Mth.clamp(amount,-power,maxCharge-power);
        boolean shouldRebuild = power==0&&amount!=0;
        power+=amount;
        toolStackView.getPersistentData().putInt(KEY_AMBROSIUM_POWER,Math.max(power,0));
        if (!shouldRebuild) shouldRebuild= power<=0;
        if (shouldRebuild)
            ((ToolStack)toolStackView).rebuildStats();
        return amount;
    }
    public static int chargeTool(IToolStackView toolStackView){
        return chargeTool(toolStackView,4);
    }

    public static void chargeLiving(LivingEntity living, int amount){
        for(EquipmentSlot slot:EquipmentSlot.values()){
            var stack = living.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable){
                amount-=chargeTool(ToolStack.from(stack));
                if (amount<=0) break;
            }
        }
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        IModDataView nbt = iToolContext.getPersistentData();
        if (nbt.getInt(KEY_AMBROSIUM_POWER)>0){
            List.of(ToolStats.ATTACK_DAMAGE,ToolStats.PROJECTILE_DAMAGE,ToolStats.ATTACK_SPEED,ToolStats.DRAW_SPEED,ToolStats.ARMOR,ToolStats.ARMOR_TOUGHNESS).forEach(stat->stat.percent(modifierStatsBuilder,0.5*modifierEntry.getLevel()));
        }
    }

    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        list.add(Component.literal("神能晶充能：").append(String.valueOf(iToolStackView.getPersistentData().getInt(KEY_AMBROSIUM_POWER))).withStyle(this.getDisplayName().getStyle()));
    }


    @Override
    public int onDamageTool(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i, @Nullable LivingEntity livingEntity) {
        if (iToolStackView.getPersistentData().getInt(KEY_AMBROSIUM_POWER)>0){
            iToolStackView.getPersistentData().putInt(KEY_AMBROSIUM_POWER,iToolStackView.getPersistentData().getInt(KEY_AMBROSIUM_POWER)-1);
            return 0;
        }
        return i;
    }



    public static int getCharge(IToolStackView tool){
        return tool.getPersistentData().getInt(KEY_AMBROSIUM_POWER);
    }
    public static int getAndUseChargeWithMax(IToolStackView toolStackView,int maxUsage){
        var charge = Math.min(getCharge(toolStackView),maxUsage);
        chargeTool(toolStackView, (int) (-0.0625f*charge));
        return charge;
    }
    public static int getAndUseCharge(IToolStackView toolStackView){
        var charge = getCharge(toolStackView);
        chargeTool(toolStackView, (int) (-0.0625f*charge));
        return charge;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        damage+=getAndUseCharge(tool)*2;
        return damage;
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, NamespacedNBT persistentData, boolean primary) {
        if (arrow!=null){
            arrow.setBaseDamage(arrow.getBaseDamage()+getAndUseCharge(tool));
        }
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        amount-=getAndUseChargeWithMax(tool, (int) amount)*3;
        return amount;
    }
}
