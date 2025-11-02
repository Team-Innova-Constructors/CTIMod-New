package com.hoshino.cti.Modifier.Replace;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class Arrogance extends BattleModifier {
    public int getPriority() {
        return 100;
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (attacker instanceof Player player && livingTarget instanceof Mob mob) {
            float var10001 = DifficultyLevel.ofAny(player);
            float var10002 = DifficultyLevel.ofAny(mob);
            if (var10002 > var10001) {
                return damage + baseDamage * Math.min((var10002 - var10001) / 50, 10);
            }
        }
        return baseDamage;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
        if (player != null) {
            float var10001 = DifficultyLevel.ofAny(player);
            list.add(Component.literal("当前玩家等级：" + var10001).withStyle(ChatFormatting.GRAY));
        }
    }
}