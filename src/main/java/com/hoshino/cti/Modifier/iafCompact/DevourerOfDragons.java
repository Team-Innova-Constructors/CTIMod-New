package com.hoshino.cti.Modifier.iafCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.hoshino.cti.Cti;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import java.util.List;

public class DevourerOfDragons extends EtSTBaseModifier {
    public static final ResourceLocation KEY_DISPLAY_BONUS = Cti.getResource("dod_bonus");
    public static float getBonus(ServerPlayer player){
        var stats = player.getStats();
        float bonus = 0;
        bonus+= stats.getValue(Stats.ENTITY_KILLED.get(IafEntityRegistry.FIRE_DRAGON.get()));
        bonus+= stats.getValue(Stats.ENTITY_KILLED.get(IafEntityRegistry.ICE_DRAGON.get()));
        bonus+= stats.getValue(Stats.ENTITY_KILLED.get(IafEntityRegistry.LIGHTNING_DRAGON.get()));
        return Math.min(bonus*0.1f,5);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker() instanceof ServerPlayer player){
            tool.getPersistentData().putFloat(KEY_DISPLAY_BONUS,getBonus(player));
            return damage*(1+getBonus(player));
        }
        return damage;
    }

    @Override
    public int getPriority() {
        return 16384;
    }

    @Override
    public void modifierOnProjectileLaunch(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, NamespacedNBT namespacedNBT, boolean primary) {
        if (projectile instanceof AbstractArrow arrow&&livingEntity instanceof ServerPlayer player){
            tool.getPersistentData().putFloat(KEY_DISPLAY_BONUS,getBonus(player));
            arrow.setBaseDamage(arrow.getBaseDamage()*(1+getBonus(player)));
        }
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        return super.getDisplayName().copy().append(" +"+String.format("%.0f",tool.getPersistentData().getFloat(KEY_DISPLAY_BONUS)*100)+"%");
    }
}
