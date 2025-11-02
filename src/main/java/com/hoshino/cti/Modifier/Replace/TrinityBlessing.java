package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.c2h6s.etshtinker.util.C;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DisplayNameModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.RomanNumeralHelper;

import static com.c2h6s.etshtinker.util.getMainOrOff.getMainLevel;
import static com.c2h6s.etshtinker.util.getMainOrOff.getOffLevel;

public class TrinityBlessing extends EtSTBaseModifier implements ToolStatsModifierHook, ToolDamageModifierHook, DisplayNameModifierHook {
    private static final TinkerDataCapability.TinkerDataKey<Integer> key = TConstruct.createKey("trinityblessing");

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.TOOL_DAMAGE, ModifierHooks.DISPLAY_NAME);
        builder.addModule(new ArmorLevelModule(key, false, (TagKey) null));
    }

    public TrinityBlessing() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity attacker = event.getEntity();
        if (attacker != null) {
            attacker.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                int level = holder.get(key, 0);
                if (level > 0 && event.getSource().isBypassArmor()) {
                    event.setCanceled(true);
                }
            });
            if (getMainLevel(attacker, this) > 0 && event.getSource().isBypassArmor()) {
                event.setCanceled(true);
            } else if (getOffLevel(attacker, this) > 0 && event.getSource().isBypassArmor()) {
                event.setCanceled(true);
            }
        }
    }

    private void livingattackevent(LivingAttackEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity attacker) {
            attacker.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                int level = holder.get(key, 0);
                if (level > 0) {
                    event.getSource().bypassArmor();
                }
            });
            if (getMainLevel(attacker, this) > 0) {
                event.getSource().bypassArmor();
            } else if (getOffLevel(attacker, this) > 0) {
                event.getSource().bypassArmor();
            }
        }
    }


    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() != 3) {
            ToolStats.DURABILITY.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.ATTACK_SPEED.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.ATTACK_DAMAGE.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.ACCURACY.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.DRAW_SPEED.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.VELOCITY.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.MINING_SPEED.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.ARMOR.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.BLOCK_AMOUNT.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolStats.BLOCK_ANGLE.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            etshtinkerToolStats.PLASMARANGE.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            etshtinkerToolStats.ENERGY_STORE.multiply(builder, Math.pow(1.67, modifier.getLevel()));
            ToolTankHelper.CAPACITY_STAT.multiply(builder, Math.pow(1.67, modifier.getLevel()));
        }
        if (modifier.getLevel() == 3) {
            ToolStats.DURABILITY.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.ATTACK_SPEED.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.ATTACK_DAMAGE.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.ACCURACY.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.DRAW_SPEED.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.VELOCITY.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.MINING_SPEED.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.ARMOR.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.BLOCK_AMOUNT.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolStats.BLOCK_ANGLE.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            etshtinkerToolStats.PLASMARANGE.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            etshtinkerToolStats.ENERGY_STORE.multiply(builder, Math.pow(3.33, modifier.getLevel()));
            ToolTankHelper.CAPACITY_STAT.multiply(builder, Math.pow(3.33, modifier.getLevel()));
        }
    }

    public int getPriority() {
        return 512;
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int i, @Nullable LivingEntity livingEntity) {
        if (modifier.getLevel() > 0) {
            return 0;
        }
        return i;
    }

    @Override
    public Component getDisplayName(IToolStackView iToolStackView, ModifierEntry modifierEntry, Component component) {
        return Component.literal(C.GetColorT(Component.translatable(this.getDisplayName().getString()).toString())).append(" ").append(RomanNumeralHelper.getNumeral(modifierEntry.getLevel()));
    }
}
