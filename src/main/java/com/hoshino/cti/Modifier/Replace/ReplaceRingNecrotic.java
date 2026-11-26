package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.util.LifeStealCooldown;
import com.xiaoyue.tinkers_ingenuity.generic.XICModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplaceRingNecrotic extends XICModifier {

    private static final float HEAL_RATIO = 0.06F;

    public void onCurioToDamage(IToolStackView curio, LivingDamageEvent event, LivingEntity attacker, LivingEntity target, int level) {
        if (!(attacker instanceof Player player) || player.level.isClientSide) {
            return;
        }
        if (LifeStealCooldown.tryTrigger(player)) {
            return;
        }
        player.heal(player.getMaxHealth() * HEAL_RATIO);
    }
}
