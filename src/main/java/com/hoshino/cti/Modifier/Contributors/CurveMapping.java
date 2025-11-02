package com.hoshino.cti.Modifier.Contributors;


import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiEffects;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.hoshino.cti.register.CtiModifiers.curvemapping;

public class CurveMapping extends BattleModifier {
    public CurveMapping() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private static final ResourceLocation curvemappingtime = Cti.getResource("curvemappingtime");

    public void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getSource().getEntity();
        if (a instanceof ServerPlayer player && event.getEntity() != null) {
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            int c = ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), curvemapping.getId());
            if (c > 0) {
                int b = (int) event.getAmount();
                event.setAmount(b);
                if (b % 2 == 0 && tool.getPersistentData().getInt(curvemappingtime) == 0) {
                    tool.getPersistentData().putInt(curvemappingtime, 300 - c * 10);
                    player.addEffect(new MobEffectInstance(CtiEffects.curve_mapping.get(), 200, c));
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            if (tool.getPersistentData().getInt(curvemappingtime) > 0) {
                tool.getPersistentData().putInt(curvemappingtime, tool.getPersistentData().getInt(curvemappingtime) - 1);
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            ModDataNBT tooldata = tool.getPersistentData();
            tooltip.add(net.minecraft.network.chat.Component.translatable("[曲线映射]的冷却还剩" + (tooldata.getInt(curvemappingtime) / 20) + "秒").withStyle(ChatFormatting.AQUA));
        }
    }
}