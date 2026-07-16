package com.hoshino.cti.util.L2;

import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class KnlyHelper {
    public static final UUID KNLY_HEALTH_REMOVER_UUID = CommonUtil.UUIDFromAnyString("knly_buried_ocean");
    private static final int MAX_LEVEL = 10;

    public static void checkAndGiveData(LivingEntity living) {
        if (living.getLevel().isClientSide()) return;
        int currentLevel = living.getPersistentData().getInt("knly_cost_health");
        int nextLevel = Math.min(MAX_LEVEL, currentLevel + 1);
        living.getPersistentData().putInt("knly_cost_health", nextLevel);
        updateHealthModifier(living);
    }

    public static void removeData(LivingEntity living) {
        if (living.getLevel().isClientSide()) return;
        int currentLevel = living.getPersistentData().getInt("knly_cost_health");
        int nextLevel = Math.max(0, currentLevel - 1);
        living.getPersistentData().putInt("knly_cost_health", nextLevel);
        updateHealthModifier(living);
    }

    private static void updateHealthModifier(LivingEntity living) {
        var maxHealthAttr = living.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr == null) return;
        maxHealthAttr.removeModifier(KNLY_HEALTH_REMOVER_UUID);
        int level = living.getPersistentData().getInt("knly_cost_health");
        if (level > 0) {
            double decreaseAmount = Math.pow(0.8, level) - 1.0;
            if (decreaseAmount <= -0.99) {
                decreaseAmount = -0.99;
            }
            var modifier = new AttributeModifier(KNLY_HEALTH_REMOVER_UUID, "海葬削减", decreaseAmount, AttributeModifier.Operation.MULTIPLY_TOTAL);
            maxHealthAttr.addTransientModifier(modifier);
        }
        if (living.getHealth() > living.getMaxHealth()) {
            living.setHealth(living.getMaxHealth());
        }
    }
    public static boolean hasBeenCost(LivingEntity living) {
        return living.getPersistentData().getInt("knly_cost_health") > 0;
    }
}
