package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class Emp extends EtSTBaseModifier implements DurabilityDisplayModifierHook {
    public static ResourceLocation charge = new ResourceLocation(Cti.MOD_ID, "emp_charge");

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.DURABILITY_DISPLAY);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onModifierRemoved(IToolStackView tool) {
        tool.getPersistentData().remove(charge);
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (tool.getPersistentData().getInt(charge) < 100 && !level.isClientSide) {
            tool.getPersistentData().putInt(charge, tool.getPersistentData().getInt(charge) + 1);
        }
        if (holder != null && holder.getPersistentData().getInt("empcd") > 0) {
            holder.getPersistentData().putInt("empcd", holder.getPersistentData().getInt("empcd") - 1);
            if (holder.getPersistentData().getInt("empcd") <= 0) {
                holder.getPersistentData().remove("empcd");
            }
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        LivingEntity living = context.getAttacker();
        if (tool.getPersistentData().getInt(charge) >= 100 && living.getPersistentData().getInt("empcd") <= 0) {
            Entity entity = context.getTarget();
            List<LivingEntity> list = entity.level.getEntitiesOfClass(LivingEntity.class, new AABB(entity.blockPosition()).inflate(24));
            for (LivingEntity target : list) {
                if (!(target instanceof Player) && target != null) {
                    target.getPersistentData().putInt("emp", 60);
                }
            }
            tool.getPersistentData().putInt(charge, 0);
            living.getPersistentData().putInt("empcd", 200);
        }
        return knockback;
    }

    @Override
    public void modifierOnProjectileLaunch(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, NamespacedNBT namespacedNBT, boolean primary) {
        if (livingEntity != null) {
            if (tool.getPersistentData().getInt(charge) >= 100 && livingEntity.getPersistentData().getInt("empcd") <= 0) {
                List<LivingEntity> list = livingEntity.level.getEntitiesOfClass(LivingEntity.class, new AABB(livingEntity.blockPosition()).inflate(24));
                for (LivingEntity target : list) {
                    if (!(target instanceof Player) && target != null) {
                        target.getPersistentData().putInt("emp", 60);
                    }
                }
                tool.getPersistentData().putInt(charge, 0);
                livingEntity.getPersistentData().putInt("empcd", 200);
            }
        }
    }

    @Nullable
    @Override
    public Boolean showDurabilityBar(IToolStackView tool, ModifierEntry modifierEntry) {
        return tool.getDamage() > 0 || tool.getPersistentData().getInt(charge) >= 100;
    }

    @Override
    public int getDurabilityWidth(IToolStackView tool, ModifierEntry modifierEntry) {
        int max = tool.getStats().getInt(ToolStats.DURABILITY);
        int amount = tool.getCurrentDurability();
        return amount >= max ? 13 : 1 + 13 * (amount - 1) / max;
    }

    @Override
    public int getDurabilityRGB(IToolStackView tool, ModifierEntry modifierEntry) {
        return tool.getPersistentData().getInt(charge) >= 100 ? 0x0000FF : -1;
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        return this.getDisplayName().copy().append(" ").append(Component.translatable("etshtinker.modifier.tooltip.charge")).append("").append(tool.getPersistentData().getInt(charge) + "%");
    }

}
