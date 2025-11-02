package com.hoshino.cti.Modifier.Contributors;


import com.hoshino.cti.Cti;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class TriangleTheory extends BattleModifier {
    public TriangleTheory() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private static final ResourceLocation triangletheorytime = Cti.getResource("triangletheorytime");

    @Override
    public boolean havenolevel() {
        return true;
    }

    public void livinghurtevent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player && event.getEntity() != null) {
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT a = tool.getPersistentData();
                    if (tool.getModifierLevel(this) > 0) {
                        if (a.getInt(triangletheorytime) == 2) {
                            a.putInt(triangletheorytime, 0);
                        } else if (a.getInt(triangletheorytime) == 0) {
                            event.setAmount(0);
                            a.putInt(triangletheorytime, a.getInt(triangletheorytime) + 1);
                        }
                    }
                }
            }
        }
    }
}