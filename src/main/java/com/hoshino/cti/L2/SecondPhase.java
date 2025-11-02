package com.hoshino.cti.L2;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class SecondPhase extends MobTrait {
    public SecondPhase(ChatFormatting format) {
        super(format);
    }
    public static final String TAG_AFTER_PHASING = "after_change_phase";

    @Override
    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
        if (entity.getTags().contains(TAG_AFTER_PHASING)) return;
        if (entity.getHealth()>event.getAmount()&&event.getAmount()>=entity.getMaxHealth()*0.5f){
            var tickerManager = EntityTickerManager.getInstance(entity);
            tickerManager.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.INVULNERABLE.get(),1,100*level));
            entity.addTag(TAG_AFTER_PHASING);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                        mapLevel(i -> Component.literal(i*5+"")
                                .withStyle(ChatFormatting.AQUA)))
                .withStyle(ChatFormatting.GRAY));
    }
}
