package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

public class NetherGobberBless extends Modifier implements MeleeDamageModifierHook, ModifyDamageModifierHook, ProjectileHitModifierHook, InventoryTickModifierHook, ProcessLootModifierHook, MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.INVENTORY_TICK, ModifierHooks.MODIFY_HURT, ModifierHooks.PROJECTILE_HIT, ModifierHooks.PROCESS_LOOT, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getAttacker() instanceof Player player && context.getLivingTarget() instanceof WitherSkeleton WS) {
            WS.die(DamageSource.playerAttack(player));
            WS.remove(Entity.RemovalReason.KILLED);
            ItemStack skull = new ItemStack(Items.WITHER_SKELETON_SKULL);
            if (tool.getItem() != TinkerTools.cleaver.get()) {
                context.getLivingTarget().spawnAtLocation(skull);
            }
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (!(projectile instanceof AbstractArrow arrow)) return false;
        var level = modifier.getLevel();
        arrow.setBaseDamage(arrow.getBaseDamage() * (1 + level * 0.25f));
        return false;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player && isCorrectSlot && player.tickCount % 60 == 0) {

            for (ItemStack itemStack1 : player.getAllSlots()) {
                if (ModifierUtil.getModifierLevel(itemStack1, this.getId()) > 0) {
                    float saturationLevel = player.getFoodData().getSaturationLevel();
                    int foodlevel = player.getFoodData().getFoodLevel();
                    player.getFoodData().setFoodLevel(Math.min(20, foodlevel + 1));
                    player.getFoodData().setSaturation(Math.min(20, saturationLevel + 1));
                }
            }
        }
        if (entity.tickCount % 200 == 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 280, 0, false, false, true));
        }
    }

    @Override
    public void processLoot(IToolStackView iToolStackView, ModifierEntry modifierEntry, List<ItemStack> list, LootContext lootContext) {
        Player player = lootContext.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        Entity target = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack skull = new ItemStack(Items.WITHER_SKELETON_SKULL);
        if (player != null && target instanceof WitherSkeleton && player.getMainHandItem().getItem() == TinkerTools.cleaver.get()) {
            int SeveringLevel = GetModifierLevel.getMainhandModifierlevel(player, TinkerModifiers.severing.getId());
            int LootLevel = lootContext.getLootingModifier();
            skull.setCount(SeveringLevel * LootLevel);
            target.spawnAtLocation(skull);
        }
    }

    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount, boolean b) {
        var level = modifierEntry.getLevel();
        return amount * (1 - (level * 0.12f));
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float v, float damage) {
        var level = modifierEntry.getLevel();
        return damage * (1 + level * 0.25f);
    }
}
