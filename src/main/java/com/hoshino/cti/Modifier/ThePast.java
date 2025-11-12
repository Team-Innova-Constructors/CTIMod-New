package com.hoshino.cti.Modifier;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiSounds;
import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class ThePast extends Modifier implements ModifyDamageModifierHook , InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE,ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public int getPriority() {
        return 10;
    }
    public static final ResourceLocation FRAGMENT_COOLDOWN = Cti.getResource("fragment_cooldown");

    private void runHurt(Mob mob , DamageSource source ,float damage){
        mob.invulnerableTime=0;
        mob.hurt(source,damage);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount, boolean b) {
        var entity=equipmentContext.getEntity();
        if (amount == 0) return amount;
        if(!(entity instanceof Player player))return amount;
        if(damageSource instanceof EntityDamageSource entityDamageSource&&entityDamageSource.isThorns())return amount;
        int lastFragmentTime = tool.getPersistentData().getInt(FRAGMENT_COOLDOWN);
        if(lastFragmentTime>0)return amount;

        tool.getPersistentData().putInt(FRAGMENT_COOLDOWN,4);
        var area = new AABB(entity.getOnPos()).inflate(3);
        List<Mob> targets = entity.level.getEntitiesOfClass(Mob.class, area, LivingEntity::isAlive);
        int increaseLevel = GetModifierLevel.getTotalArmorModifierlevel(entity, CtiModifiers.MANDARIN_DUCK_STATIC_MODIFIER.getId());
        boolean hasIncrease = increaseLevel > 0;
        boolean shouldHurtOwner = entity.level.getRandom().nextBoolean();
        var source = new EntityDamageSource("obsidianhurt", entity).setThorns();
        float baseDurability=0;
        for(ItemStack stack:player.getInventory().armor){
            baseDurability= baseDurability+ToolStack.from(stack).getCurrentDurability();
        }
        float baseDamage=baseDurability * 0.01f;
        if (shouldHurtOwner && !hasIncrease) {
            entity.hurt(source, baseDamage + modifier.getLevel());
            targets.forEach(mob -> runHurt(mob,source,baseDamage *modifier.getLevel()));
        } else if (hasIncrease) {
            targets.forEach(mob -> runHurt(mob,source.bypassArmor(), baseDamage *modifier.getLevel() * 1.8f * increaseLevel));
        } else {
            targets.forEach(mob -> runHurt(mob,source, baseDamage *modifier.getLevel()));
        }
        entity.level.playSound(null, entity.getOnPos(), CtiSounds.armor_broken.get(), SoundSource.AMBIENT, 1, 1);
        return amount;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(livingEntity.tickCount%20==0){
            int lastFragmentTime = iToolStackView.getPersistentData().getInt(FRAGMENT_COOLDOWN);
            if(lastFragmentTime>0){
                iToolStackView.getPersistentData().putInt(FRAGMENT_COOLDOWN,lastFragmentTime-1);
            }
        }
    }
}
