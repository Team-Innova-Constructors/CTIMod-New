package com.hoshino.cti.L2;

import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class TemporaryArmor extends LegendaryTrait {
    public static String KEY_REDUCTION = "reactive_reduction";
    public TemporaryArmor(ChatFormatting format) {
        super(format);
    }

    @Override
    public void initialize(LivingEntity mob, int level) {
        mob.getPersistentData().putInt(KEY_REDUCTION,100);
    }

    @Override
    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
        CompoundTag nbt = entity.getPersistentData();
        if (nbt.getInt(KEY_REDUCTION)>0){
            event.setAmount(event.getAmount()*(1-0.01f*nbt.getInt(KEY_REDUCTION)));
            nbt.putInt(KEY_REDUCTION,Math.max(0,nbt.getInt(KEY_REDUCTION)-(event.getSource().isBypassArmor()?2:1)));
        }
    }
}
