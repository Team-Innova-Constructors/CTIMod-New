package com.hoshino.cti.Modifier.Contributors;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class ArcaneFilling extends BattleModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getSource().isMagic() && event.getSource().getEntity() instanceof Player player) {
            if (ModifierLevel.EquipHasModifierlevel(player, this.getId())) {
                event.setAmount(event.getAmount() * 2F);
            }
        }
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (attacker instanceof Player player) {
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            int range = level * 2;
            List<Mob> mobbbb = player.level.getEntitiesOfClass(Mob.class, new AABB(x + range, y + range, z + range, x - range, y - range, z - range));
            for (Mob target : mobbbb) {
                target.hurt(DamageSource.playerAttack(player).bypassArmor().setMagic(), damage);
            }
        }
        return damage;
    }
}
