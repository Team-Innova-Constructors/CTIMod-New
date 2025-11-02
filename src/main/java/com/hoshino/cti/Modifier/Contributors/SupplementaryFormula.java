package com.hoshino.cti.Modifier.Contributors;


import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiEffects;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class SupplementaryFormula extends BattleModifier {
    public SupplementaryFormula() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private static final ResourceLocation supplementaryformulatime = Cti.getResource("supplementaryformulatime");

    public void livinghurtevent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT a = tool.getPersistentData();
                    if (a.getInt(supplementaryformulatime) == 0) {
                        if (tool.getModifierLevel(this) > 0 && tool.getModifierLevel(this) < 8) {
                            a.putInt(supplementaryformulatime, 37);
                            player.addEffect(new MobEffectInstance(CtiEffects.curve_mapping.get(), 200, tool.getModifierLevel(this) - 1));
                        }
                        if (tool.getModifierLevel(this) >= 8) {
                            a.putInt(supplementaryformulatime, 37);
                            player.addEffect(new MobEffectInstance(CtiEffects.curve_mapping.get(), 200, 7));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            if (tool.getPersistentData().getInt(supplementaryformulatime) > 0 && player.tickCount % 20 == 0) {
                tool.getPersistentData().putInt(supplementaryformulatime, tool.getPersistentData().getInt(supplementaryformulatime) - 1);
            }
        }
    }
}