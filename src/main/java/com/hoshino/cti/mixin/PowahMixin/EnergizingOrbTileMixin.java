package com.hoshino.cti.mixin.PowahMixin;

import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.lib.logistics.inventory.Inventory;

@Mixin(value = EnergizingOrbTile.class,remap = false)
public class EnergizingOrbTileMixin {
    @Redirect(method = "fillEnergy",at = @At(value = "INVOKE", target = "Lowmii/powah/lib/logistics/inventory/Inventory;clear()V"))
    private void avoidSlotSeal(Inventory instance){
        for (int i=0;i<instance.getSlots();i++){
            var item = instance.getStackInSlot(i).getItem();
            var seal = ForgeRegistries.ITEMS.getValue(CommonUtil.SLOT_SEAL);
            if (item!=seal) instance.setStackInSlot(i,ItemStack.EMPTY);
        }
    }
}
