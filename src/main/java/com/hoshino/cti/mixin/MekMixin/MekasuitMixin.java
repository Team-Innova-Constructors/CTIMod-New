package com.hoshino.cti.mixin.MekMixin;

import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import mekanism.common.item.gear.ItemSpecialArmor;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.lib.attribute.IAttributeRefresher;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author <h3>牢墨<h3/>
 * <h5>修改了mekasuit的防御面，现在会处理并正常免疫所有伤害</h5>
 */
@Mixin(value = ItemMekaSuitArmor.class, remap = false)
public abstract class MekasuitMixin extends ItemSpecialArmor implements IModuleContainerItem, IModeItem, IJetpackItem, IAttributeRefresher {
    protected MekasuitMixin(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }
    @Redirect(
            method = "getDamageAbsorbed(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/damagesource/DamageSource;FLjava/util/List;)F",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/DamageSource;m_19376_()Z"
            ))
    private static boolean damageAbsorbed(DamageSource instance) {
        return false;
    }
}
