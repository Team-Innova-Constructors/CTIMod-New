package com.hoshino.cti.util.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.UUID;

public class CorodiHelper {
    public static final UUID CORODI_ARMOR_REMOVER_UUID = UUID.nameUUIDFromBytes("corrosion".getBytes());

    public static void checkAndGiveData(Player player, int traitLevel) {
        if (player.getLevel().isClientSide()) return;
        int levelStep = Math.max(1, Math.min(6, traitLevel));
        int industrialLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("cti:industrial_armor"));
        int refinedLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("cti:refined"));
        int totalShield = industrialLevel + refinedLevel;
        int actualAdd = Math.max(0, levelStep - totalShield);
        int currentLevel = player.getPersistentData().getInt("corrosion_cost_armor");
        int nextLevel = currentLevel + actualAdd;
        player.getPersistentData().putInt("corrosion_cost_armor", nextLevel);
        updateArmorModifier(player);
    }

    public static void removeData(Player player) {
        if (player.getLevel().isClientSide()) return;
        int currentLevel = player.getPersistentData().getInt("corrosion_cost_armor");
        int nextLevel = Math.max(0, currentLevel - 2);
        player.getPersistentData().putInt("corrosion_cost_armor", nextLevel);
        updateArmorModifier(player);
    }
    
    private static void updateArmorModifier(Player player) {
        var maxArmorAttr = player.getAttribute(Attributes.ARMOR);
        if (maxArmorAttr == null) return;
        maxArmorAttr.removeModifier(CORODI_ARMOR_REMOVER_UUID);
        int currentLevel = player.getPersistentData().getInt("corrosion_cost_armor");
        if (currentLevel > 0) {
            double decreaseAmount = -0.01 * currentLevel;
            if (decreaseAmount <= -0.99) {
                decreaseAmount = -0.99;
            }
            if (decreaseAmount < 0.0) {var modifier = new AttributeModifier(CORODI_ARMOR_REMOVER_UUID, "腐蚀护甲削减", decreaseAmount, AttributeModifier.Operation.MULTIPLY_TOTAL);
                maxArmorAttr.addTransientModifier(modifier);
            }
        }
    }

    public static boolean hasBeenCost(Player player) {
        return player.getPersistentData().getInt("corrosion_cost_armor") > 0;
    }
}