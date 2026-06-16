package com.hoshino.cti.Modifier.Developer;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.util.slotUtil;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiToolStats;
import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class All extends EtSTBaseModifier implements DamageBlockModifierHook,ToolDamageModifierHook{
    @Override
    public boolean isNoLevels() {
        return true;
    }

    public static List<MobEffect> ls = new ArrayList<>(List.of());

    public static void init() {
        Iterator<Potion> iterator = ForgeRegistries.POTIONS.iterator();
        if (iterator.hasNext()) {
            Potion potion = iterator.next();
            potion.getEffects().forEach((mobEffectInstance -> {
                if (mobEffectInstance.getEffect().getCategory()==MobEffectCategory.HARMFUL) ls.add(mobEffectInstance.getEffect());
            }));
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        gatherEffects(context.getPlayerAttacker(),context.getLivingTarget(),damage);
        return knockback;
    }

    @Override
    public boolean modifierOnProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (projectile instanceof AbstractArrow arrow)
            gatherEffects(attacker instanceof Player player?player:null,target, (float) arrow.getBaseDamage());
        return false;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return (float) (damage*Math.pow(10,GetModifierLevel.getAllSlotModifierlevel(context.getAttacker(),modifier.getId())+GetModifierLevel.CurioModifierLevel(context.getAttacker(),modifier.getId())));
    }

    public static void gatherEffects(Player player, LivingEntity target, float amount){
        if (player==null||target==null) return;
        for (EquipmentSlot slot : slotUtil.ALL) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable) {
                ToolStack tool = ToolStack.from(stack);
                if (tool.getModifierLevel(CtiModifiers.all.getId()) > 0) {
                    StellarBlade.summonStars(player);
                    if ( !(target instanceof Player)) {
                        CompoundTag tag = target.getPersistentData();
                        tag.putBoolean("vulnerable", true);
                        if (!tag.contains("dmg_amplifier")) {
                            tag.putFloat("dmg_amplifier", 1.5f);
                        } else {
                            tag.putFloat("dmg_amplifier", Math.max(1.5f, tag.getFloat("dmg_amplifier") + 0.5f));
                        }

                        if (!tag.contains("legacyhealth")) {
                            tag.putFloat("legacyhealth", target.getHealth() - amount);
                        } else {
                            if (target.getHealth() > tag.getFloat("legacyhealth")) {
                                target.setHealth(tag.getFloat("legacyhealth"));
                            }
                            tag.putFloat("legacyhealth", tag.getFloat("legacyhealth") - amount);
                        }

                        if (!tag.contains("atomic_dec")) {
                            tag.putFloat("atomic_dec", 20f);
                        } else {
                            tag.putFloat("atomic_dec", Math.max(20, tag.getFloat("atomic_dec") + 20));
                        }

                        if (!tag.contains("quark_disassemble")) {
                            tag.putFloat("quark_disassemble", 20f);
                        } else {
                            tag.putFloat("quark_disassemble", Math.max(20, tag.getFloat("quark_disassemble") + 20));
                        }
                        if (ls != null && !ls.isEmpty()) {
                            int i = 0;
                            while (i < 10) {
                                MobEffect effect = ls.get(EtSHrnd().nextInt(ls.size()));
                                target.forceAddEffect(new MobEffectInstance(effect, 200, 9, false, false), player);
                                i++;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK,ModifierHooks.TOOL_DAMAGE);
    }

    @Override
    public int getPriority() {
        return 512;
    }

    @Override
    public int onDamageTool(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i, @Nullable LivingEntity livingEntity) {
        return 0;
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        CtiToolStats.ELECTRIC_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.SCORCH_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.FROZEN_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.PRESSURE_RESISTANCE.add(modifierStatsBuilder, 50);
    }

    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount) {
        return true;
    }
}
