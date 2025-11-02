package com.hoshino.cti.Modifier.Contributors;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Subspace extends ArmorModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && GetModifierLevel.EquipHasModifierlevel(player, this.getId())) {
            if (event.getSource().isMagic() || event.getSource().isProjectile()) {
                event.setAmount(event.getAmount() * 0.4f);
            }
        }
    }
}
