package com.hoshino.cti.util.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Unique;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public class ExHurtHelper {
    public static float getScale(LivingEntity living){
        if(!(living instanceof Player player)) return 1;
        var relicID=new ModifierId("cti:the_relic");
        var shadowOfVigridID= new ModifierId("cti:shadow_of_vigrid");
        var relicLevel = GetModifierLevel.getTotalArmorModifierlevel(player,relicID)+GetModifierLevel.getTotalHandsModifierLevelWithShield(player,relicID);
        var shadowOfVigridLevel = GetModifierLevel.getTotalArmorModifierlevel(player,shadowOfVigridID)+GetModifierLevel.getTotalHandsModifierLevelWithShield(player,shadowOfVigridID);
        boolean hashardLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("etshtinker:solidex")) > 0;
        boolean hasEtherLevel=GetModifierLevel.curioHasModifierLevel(player,new ModifierId("solidarytinker:ether"));
        float hasHard = hashardLevel ? 0.8f : 1.0f;
        float hasEther=hasEtherLevel?0.9f:1.0f;
        float relicFactor = Math.max(0.0f, 1.0f - (relicLevel * 0.03f));
        float shadowFactor = Math.max(0.0f, 1.0f - (shadowOfVigridLevel * 0.07f));
        return relicFactor * shadowFactor * hasHard * hasEther;
    }
    public static boolean shouldHurt(LivingEntity living){
        if(!(living instanceof Player player))return true;
        if(player.isCreative()||player.isSpectator())return false;
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(MekanismItems.MEKASUIT_HELMET.get())
                || player.getItemBySlot(EquipmentSlot.CHEST).is(MekanismItems.MEKASUIT_BODYARMOR.get())
                || player.getItemBySlot(EquipmentSlot.LEGS).is(MekanismItems.MEKASUIT_PANTS.get())
                || player.getItemBySlot(EquipmentSlot.FEET).is(MekanismItems.MEKASUIT_BOOTS.get())) {
            return false;
        }
        if(GetModifierLevel.curioHasModifierLevel(player,new ModifierId("solidarytinker:bha")))return false;
        if(GetModifierLevel.equipHasModifierLevel(player,new ModifierId("tinkers_ingenuity:unmatched")))return false;
        if(GetModifierLevel.equipHasModifierLevel(player,new ModifierId("cti:eventually")))return false;
        var relicLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("cti:the_relic"));
        var shadowOfVigridLevel = GetModifierLevel.getTotalArmorModifierlevel(player, new ModifierId("cti:shadow_of_vigrid"));

        float relicFactor = Math.max(0.0f, 1.0f - (relicLevel * 0.03f));
        float shadowFactor = Math.max(0.0f, 1.0f - (shadowOfVigridLevel * 0.07f));

        if(!GetModifierLevel.equipHasModifierLevel(player, new ModifierId("cti:all")))return false;
        return relicFactor != 0 && shadowFactor != 0;
    }
}
