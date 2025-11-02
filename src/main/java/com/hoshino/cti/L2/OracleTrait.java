package com.hoshino.cti.L2;

import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.EquipmentUtil;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import quek.undergarden.registry.UGDimensions;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class OracleTrait extends MobTrait {
    public OracleTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
        for (EquipmentSlot slot:EquipmentUtil.ARMOR){
            ItemStack stack = target.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable){
                if (ToolStack.from(stack).getModifierLevel(CtiModifiers.ARMOR_ORACLE.get())>0){
                    return;
                }
            }
        }
        EntityTickerManager.getInstance(target).addTicker(
                new EntityTickerInstance(CtiEntityTickers.ORACLE.get(),1,200),
                Integer::max,
                Integer::sum);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return le.level.dimension()== AetherDimensions.AETHER_LEVEL||le.level.dimension()== UGDimensions.UNDERGARDEN_LEVEL;
    }
}
