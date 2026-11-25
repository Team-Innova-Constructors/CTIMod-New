package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.concurrent.atomic.AtomicInteger;

public class NarrowEscapeModifier extends EtSTBaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide&&level.getGameTime()%5==0){
            var points = new AtomicInteger(-1);
            holder.getActiveEffects().forEach(mobEffectInstance ->{
                if (mobEffectInstance.getEffect().getCategory()== MobEffectCategory.HARMFUL)
                    points.addAndGet(1);
            });
            EntityTickerManager.getInstance(holder).getAllTickers().forEach(instance->{
                if (instance.ticker.category==MobEffectCategory.HARMFUL)
                    points.addAndGet(1);
            });
            if (points.get()>=0){
                holder.addEffect(new MobEffectInstance(CtiEffects.NARROW_ESCAPE.get(),6,points.get(),false,false));
            }
        }
    }
}
