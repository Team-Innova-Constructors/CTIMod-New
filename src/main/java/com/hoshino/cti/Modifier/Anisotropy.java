package com.hoshino.cti.Modifier;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class Anisotropy extends NoLevelsModifier {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event){
        LivingEntity living = event.getEntity();
        if(GetModifierLevel.EquipHasModifierlevel(living, CtiModifiers.ANISOTROPY.getId())){
            event.setAmount(event.getAmount()>10?event.getAmount()*0.7f:event.getAmount()-7);
        }
    }
}
