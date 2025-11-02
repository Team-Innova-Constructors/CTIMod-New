package com.hoshino.cti.Modifier.Contributors;


import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import tcintegrations.util.BotaniaHelper;
import vazkii.botania.api.mana.ManaItemHandler;

public class ArcaneTough extends BattleModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    public int getManaPerDamage(ServerPlayer player) {
        return BotaniaHelper.getManaPerDamageBonus(player, 60);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof ServerPlayer player) {
            if (tool.getDamage() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, this.getManaPerDamage(player) * 2, true)) {
                tool.setDamage(tool.getDamage() - 10);
                player.giveExperiencePoints(100);
            }
            player.giveExperiencePoints(5);
            if (player.tickCount % 100 == 0) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 4, true, true));
            }
        }
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity entity) {
        return 0;
    }
}
