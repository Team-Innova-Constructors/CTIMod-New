package com.hoshino.cti.Modifier.Contributors;


import com.hoshino.cti.Cti;
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
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;

public class TriangleTheory extends BattleModifier {
    public TriangleTheory() {
    }

    private static final ResourceLocation triangle_theory_time = Cti.getResource("triangle_theory_time");

    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(triangle_theory_time);
        return null;
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getSource().getEntity() != null) {
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT a = tool.getPersistentData();
                    int c = tool.getModifierLevel(this);
                    if (c > 0) {
                        if (a.getInt(triangle_theory_time) < 2) {
                            event.setAmount(0);
                            a.putInt(triangle_theory_time, a.getInt(triangle_theory_time) + 1);
                        } else {
                            a.putInt(triangle_theory_time, 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            ModDataNBT data = iToolStackView.getPersistentData();
            int i = 0;
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT a = tool.getPersistentData();
                    int c = tool.getModifierLevel(this);
                    if (c > 0) {
                        i += a.getInt(triangle_theory_time);
                    }
                }
            }
            if (i>=3){
                data.putInt(triangle_theory_time,0);
            }
        }
    }
}