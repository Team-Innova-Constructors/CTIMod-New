package com.hoshino.cti.util.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.UUID;

public class RagnarokHelper {
    public static final UUID RAGNAROK_HEALTH_REMOVER_UUID = UUID.fromString("d8f4e2a1-7b3c-4a5d-9e2f-8c6b3a1f4e5d");

    public static void checkAndGiveData(Player player, int traitLevel) {
        if (player.getLevel().isClientSide()) return;
        int levelStep = Math.max(1, Math.min(3, traitLevel));
        int vigridLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("cti:shadow_of_vigrid"));

        int currentLevel = player.getPersistentData().getInt("ragnarok_cost_health");
        int nextLevel = currentLevel + levelStep;

        double maxDecrease = (-0.20 * levelStep) + (0.20 * vigridLevel);
        if (maxDecrease > 0.0) {
            maxDecrease = 0.0;
        }
        int maxAllowedLevel = (int) (Math.abs(maxDecrease) * 100);
        if (nextLevel > maxAllowedLevel) {
            nextLevel = maxAllowedLevel;
        }
        player.getPersistentData().putInt("ragnarok_cost_health", nextLevel);

        var maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.removeModifier(RAGNAROK_HEALTH_REMOVER_UUID);
            double decreaseAmount = -0.01 * nextLevel;

            if (decreaseAmount < 0.0) {
                var modifier = new AttributeModifier(RAGNAROK_HEALTH_REMOVER_UUID, "诸神削减", decreaseAmount, AttributeModifier.Operation.MULTIPLY_TOTAL);
                maxHealthAttr.addTransientModifier(modifier);
            }

            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    public static void removeData(Player player) {
        if (player.getLevel().isClientSide()) return;
        int currentLevel = player.getPersistentData().getInt("ragnarok_cost_health");
        int nextLevel = Math.max(0, currentLevel - 1);
        player.getPersistentData().putInt("ragnarok_cost_health", nextLevel);
        var maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.removeModifier(RAGNAROK_HEALTH_REMOVER_UUID);
            if (nextLevel > 0) {
                double decreaseAmount = -0.01 * nextLevel;
                if (decreaseAmount <= -0.99) {
                    decreaseAmount = -0.99;
                }
                var modifier = new AttributeModifier(RAGNAROK_HEALTH_REMOVER_UUID, "诸神削减", decreaseAmount, AttributeModifier.Operation.MULTIPLY_TOTAL);
                maxHealthAttr.addTransientModifier(modifier);
            }
            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    public static boolean hasBeenCost(Player player){
        return player.getPersistentData().getInt("ragnarok_cost_health")>0;
    }
}
