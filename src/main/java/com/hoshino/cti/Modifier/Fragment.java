package com.hoshino.cti.Modifier;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiSounds;
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
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class Fragment extends Modifier implements OnAttackedModifierHook, InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED,ModifierHooks.INVENTORY_TICK);
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
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(livingEntity.tickCount%20==0){
            int lastFragmentTime = iToolStackView.getPersistentData().getInt(FRAGMENT_COOLDOWN);
            if(lastFragmentTime>0){
                iToolStackView.getPersistentData().putInt(FRAGMENT_COOLDOWN,lastFragmentTime-1);
            }
        }
    }

    @Override
    public void onAttacked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        var entity=equipmentContext.getEntity();
        if(!(entity instanceof Player))return;
        if(equipmentSlot!=EquipmentSlot.CHEST)return;
        if(damageSource instanceof EntityDamageSource entityDamageSource&&entityDamageSource.isThorns())return;
        int lastFragmentTime = iToolStackView.getPersistentData().getInt(FRAGMENT_COOLDOWN);
        if(lastFragmentTime>0)return;

        iToolStackView.getPersistentData().putInt(FRAGMENT_COOLDOWN,3);
        var area = new AABB(entity.getOnPos()).inflate(8);
        List<Mob> targets = entity.level.getEntitiesOfClass(Mob.class, area, LivingEntity::isAlive);
        var source = new EntityDamageSource("obsidianhurt", entity).setThorns().bypassArmor();
        float DurabilityDamage=iToolStackView.getCurrentDurability() * 0.02f;
        float baseDamage=DurabilityDamage + entity.getMaxHealth() * 0.2f + entity.getArmorValue() * 0.25f;
        targets.forEach(mob -> runHurt(mob,source, Math.min(baseDamage ,50 ) * modifierEntry.getLevel()));
        entity.level.playSound(null, entity.getOnPos(), CtiSounds.armor_broken.get(), SoundSource.AMBIENT, 0.6f, 1);
    }
}
