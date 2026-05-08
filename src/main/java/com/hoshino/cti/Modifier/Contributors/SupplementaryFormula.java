package com.hoshino.cti.Modifier.Contributors;


import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiModifiers;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;

public class SupplementaryFormula extends BattleModifier {
    public SupplementaryFormula() {
    }

    private static final ResourceLocation supplementary_formula_time = Cti.getResource("supplementary_formula_time");

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(supplementary_formula_time);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            ModDataNBT data = iToolStackView.getPersistentData();
            if (data.getInt(supplementary_formula_time) > 0 && player.tickCount % 20 == 0) {
                data.putInt(supplementary_formula_time, data.getInt(supplementary_formula_time) - 1);
            }
            if (data.getInt(supplementary_formula_time) == 0) {
                int i = 0;
                for (ItemStack stack : player.getInventory().armor) {
                    if (stack.getItem() instanceof ModifiableArmorItem) {
                        ToolStack tool = ToolStack.from(stack);
                        ModDataNBT a = tool.getPersistentData();
                        int c = tool.getModifierLevel(this);
                        if (c > 0) {
                            i += c;
                            a.putInt(supplementary_formula_time, 37);
                        }
                    }
                }
                if (i > 0) {
                    if (i > 8) i = 8;
                    player.addEffect(new MobEffectInstance(CtiEffects.solid_armor.get(), 200, i));
                }
            }
        }
    }
}