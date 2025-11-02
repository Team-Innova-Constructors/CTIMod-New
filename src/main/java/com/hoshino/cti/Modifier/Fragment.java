package com.hoshino.cti.Modifier;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class Fragment extends BattleModifier{
    @Override
    public int getPriority() {
        return 10;
    }
    public static final ResourceLocation KEY_LAST_FRAGMENT = Cti.getResource("last_fragment");

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity entity) {
        if (amount == 0 || entity == null) return amount;
        int lastFragmentTime = tool.getPersistentData().getInt(KEY_LAST_FRAGMENT);
        if ((int) entity.level.getGameTime()%100==lastFragmentTime) return amount;
        tool.getPersistentData().putInt(KEY_LAST_FRAGMENT, (int) (entity.level.getGameTime()%100));
        var area = new AABB(entity.getOnPos()).inflate(3);
        List<Mob> targets = entity.level.getEntitiesOfClass(Mob.class, area, LivingEntity::isAlive);
        int increaseLevel = GetModifierLevel.getTotalArmorModifierlevel(entity, CtiModifiers.wearproofStaticModifier.getId());
        boolean hasIncrease = increaseLevel > 0;
        boolean shouldHurtOwner = entity.level.getRandom().nextBoolean();
        var source = new EntityDamageSource("obsidianhurt", entity).setThorns();
        if (shouldHurtOwner && !hasIncrease) {
            entity.hurt(source, amount * (2 + modifier.getLevel()));
            targets.forEach(mob -> mob.hurt(source, amount * (2 + modifier.getLevel())));
        } else if (hasIncrease) {
            targets.forEach(mob -> mob.hurt(source.bypassArmor(), amount * (2 + modifier.getLevel()) * 2.8f * increaseLevel));
        } else {
            targets.forEach(mob -> mob.hurt(source, amount * (2 + modifier.getLevel())));
        }
        entity.level.playSound(null, entity.getOnPos(), SoundEvents.GLASS_BREAK, SoundSource.AMBIENT, 1, 1);
        return amount * 4;
    }
}
