package com.hoshino.cti.Modifier.Contributors;

import com.c2h6s.etshtinker.init.etshtinkerEffects;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.solidarytinker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class RoxyLightning extends BattleModifier {
    private static ResourceLocation KEY = solidarytinker.getResource("key");

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(KEY);
        return null;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity player = context.getPlayerAttacker();
        ModDataNBT ToolData = tool.getPersistentData();
        int cert = ToolData.getInt(KEY);
        if (player != null && target != null) {
            if (cert > 0) {
                target.hurt(DamageSource.LIGHTNING_BOLT, player.getMaxHealth() * 10);
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, modifier.getLevel(), true, true));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, modifier.getLevel(), true, true));
                player.addEffect(new MobEffectInstance(etshtinkerEffects.ionized.get(), 200, modifier.getLevel(), true, true));
                ToolData.putInt(KEY, cert - 1);
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT ToolData = tool.getPersistentData();
        int cert = ToolData.getInt(KEY);
        if (entity.tickCount % 100 == 0) {
            ToolData.putInt(KEY, Math.min(cert + 1, modifier.getLevel() + 2));
        }
    }
}
