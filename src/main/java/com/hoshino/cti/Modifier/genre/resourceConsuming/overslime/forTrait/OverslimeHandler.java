package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.forTrait;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Entity.Projectiles.GelCloudEntity;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiToolStats;
import com.hoshino.cti.util.AttackUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.hoshino.cti.content.materialGenre.GenreManager.OVERSLIME_GENRE;

//用ModifierTraitModule添加的隐藏词条，负责黏液流增伤/减伤的计算
public class OverslimeHandler extends Modifier implements MeleeDamageModifierHook , MeleeHitModifierHook , ModifyDamageModifierHook, ProtectionModifierHook {
    public static final int OVERSLIME_MODIFIER_PRIORITY = 150;
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_ARMOR = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("overslime_armor"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT,ModifierHooks.MELEE_DAMAGE,ModifierHooks.PROTECTION,ModifierHooks.MODIFY_HURT);
        hookBuilder.addModule(new ArmorLevelModule(KEY_ARMOR,false,null));
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    public int getPriority() {
        return OVERSLIME_MODIFIER_PRIORITY;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var stats = tool.getStats();
        float consumption = stats.getInt(OVERSLIME_GENRE.consumption);
        float efficiency = stats.getInt(CtiToolStats.EFFICIENCY);
        float power = stats.getInt(CtiToolStats.POWER);
        consumption*=power/efficiency;
        int actualConsumption = (int) consumption+ (RANDOM.nextFloat()<consumption-(int)consumption ? 1 : 0);
        var overslime = TinkerModifiers.overslime.get();
        if (overslime.getShield(tool)<actualConsumption) return damage;
        float baseBonus = stats.get(OVERSLIME_GENRE.baseStat)*power;
        float mulBonus = stats.get(OVERSLIME_GENRE.mulStat)*power;
        damage += baseBonus;
        damage += damage*mulBonus;
        overslime.addOverslime(tool,modifier,-actualConsumption);
        return damage;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        var base = tool.getStats().get(OVERSLIME_GENRE.baseArmorStat);
        var consumption = tool.getStats().getInt(OVERSLIME_GENRE.consumption);
        var os = TinkerModifiers.overslime.get();
        var remain = os.getShield(tool);
        if (remain>=consumption&&amount>0){
            amount-=base;
            os.addOverslime(tool,modifier,-consumption);
        }
        return amount;
    }

    @Override
    public float getProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float modifierValue) {
        var needed = ProtectionModifierHook.getProtectionCap(context.getEntity().getCapability(TinkerDataCapability.CAPABILITY))-modifierValue;
        var base = tool.getStats().get(OVERSLIME_GENRE.mulArmorStat)/0.04f;
        var consumption = tool.getStats().getInt(OVERSLIME_GENRE.consumption);
        var os = TinkerModifiers.overslime.get();
        var remain = os.getShield(tool);
        if (needed>0&& remain>=consumption){
            modifierValue+=base;
            os.addOverslime(tool,modifier,-consumption);
        }
        return modifierValue;
    }

}
