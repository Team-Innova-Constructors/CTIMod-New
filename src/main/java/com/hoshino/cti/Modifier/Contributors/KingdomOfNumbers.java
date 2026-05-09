package com.hoshino.cti.Modifier.Contributors;


import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiEffects;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;

import static com.hoshino.cti.register.CtiModifiers.kingdomofnumbers;

public class KingdomOfNumbers extends BattleModifier {
    public KingdomOfNumbers() {
    }

    private static final ResourceLocation numerical_perception_time = Cti.getResource("numerical_perception_time");

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(numerical_perception_time);
        return null;
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        Entity a = event.getSource().getEntity();
        if (a instanceof Player player && event.getEntity() != null) {
            if (player.getMainHandItem().getItem() instanceof IModifiable) {
                ToolStack tool = ToolStack.from(player.getMainHandItem());
                int c = ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), kingdomofnumbers.getId());
                if (c > 0) {
                    int b = (int) Math.floor(event.getAmount());
                    if (b % 2 != 1 && tool.getPersistentData().getInt(numerical_perception_time) == 0) {
                        tool.getPersistentData().putInt(numerical_perception_time, 300 - c * 10);
                        player.addEffect(new MobEffectInstance(CtiEffects.numerical_perception.get(), 200, c));
                    }
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer) {
            ModDataNBT data = iToolStackView.getPersistentData();
            if (data.getInt(numerical_perception_time) > 0) {
                data.putInt(numerical_perception_time, data.getInt(numerical_perception_time) - 1);
            }
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int[] originColor=new int[]{0x3AEEFF,0xB7E8EF,0xC1DEF5,0x389DE3};
        if (player != null) {
            ModDataNBT data = tool.getPersistentData();
            tooltip.add(DynamicComponentUtil.scrollColorfulText.getColorfulText("[数于几何的王国]的冷却还剩" ,(data.getInt(numerical_perception_time) / 20) + "秒",originColor,40,40,false));
        }
    }
}