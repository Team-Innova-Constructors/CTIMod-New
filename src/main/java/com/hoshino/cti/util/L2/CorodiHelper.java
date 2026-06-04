package com.hoshino.cti.util.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.UUID;

public class CorodiHelper{
    public static final UUID CORODI_ARMOR_REMOVER_UUID = UUID.nameUUIDFromBytes("corodi".getBytes());

    public static void checkAndGiveData(Player player, int traitLevel) {
        if (player.getLevel().isClientSide()) return;
        int levelStep = Math.max(1, Math.min(6, traitLevel));
        int industrialLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("cti:industrial_armor"));
        int refinedLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("cti:refined"));
        int currentLevel = player.getPersistentData().getInt("corodi_cost_armor");
        int nextLevel = currentLevel + levelStep;

        double maxDecrease = (-0.16 * levelStep) + (0.8 * industrialLevel) + (0.16 * refinedLevel);
        if (maxDecrease > 0.0) {
            maxDecrease = 0.0;
        }
        int maxAllowedLevel = (int) (Math.abs(maxDecrease) * 100);
        if (nextLevel > maxAllowedLevel) {
            nextLevel = maxAllowedLevel;
        }
        player.getPersistentData().putInt("corodi_cost_armor", nextLevel);

        var maxArmorAttr = player.getAttribute(Attributes.ARMOR);
        if (maxArmorAttr != null) {
            maxArmorAttr.removeModifier(CORODI_ARMOR_REMOVER_UUID);
            double decreaseAmount = -0.01 * nextLevel;

            if (decreaseAmount < 0.0) {
                var modifier = new AttributeModifier(CORODI_ARMOR_REMOVER_UUID, "腐蚀护甲削减", decreaseAmount, AttributeModifier.Operation.MULTIPLY_TOTAL);
                maxArmorAttr.addTransientModifier(modifier);
            }

            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    public static void removeData(Player player) {
        if (player.getLevel().isClientSide()) return;
        int currentLevel = player.getPersistentData().getInt("corodi_cost_armor");
        int nextLevel = Math.max(0, currentLevel - 2);
        player.getPersistentData().putInt("corodi_cost_armor", nextLevel);
        var maxHealthAttr = player.getAttribute(Attributes.ARMOR);
        if (maxHealthAttr != null) {
            maxHealthAttr.removeModifier(CORODI_ARMOR_REMOVER_UUID);
            if (nextLevel > 0) {
                double decreaseAmount = -0.01 * nextLevel;
                if (decreaseAmount <= -0.99) {
                    decreaseAmount = -0.99;
                }
                var modifier = new AttributeModifier(CORODI_ARMOR_REMOVER_UUID, "腐蚀护甲削减", decreaseAmount, AttributeModifier.Operation.MULTIPLY_TOTAL);
                maxHealthAttr.addTransientModifier(modifier);
            }
            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    public static boolean hasBeenCost(Player player){
        return player.getPersistentData().getInt("corodi_cost_armor")>0;
    }
}
