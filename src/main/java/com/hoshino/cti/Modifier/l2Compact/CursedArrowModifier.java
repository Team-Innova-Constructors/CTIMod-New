package com.hoshino.cti.Modifier.l2Compact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class CursedArrowModifier extends EtSTBaseModifier {
    @Override
    public boolean modifierOnProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target!=null){
            EffectUtil.addEffect(target,new MobEffectInstance(LCEffects.CURSE.get(),100*modifier.getLevel(),0), EffectUtil.AddReason.FORCE,attacker);
        }
        return false;
    }
}
