package com.hoshino.cti.Modifier.Race;

import com.hoshino.cti.Cti;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EndlessHatred extends Modifier implements InventoryTickModifierHook {
    public static final TinkerDataCapability.TinkerDataKey<Integer> ENDLESS_HATRED = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("endless_hatred"));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
        hookBuilder.addModule(new ArmorLevelModule(ENDLESS_HATRED,false,null));
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(!b1)return;
        if(livingEntity.tickCount%60!=0)return;
        livingEntity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((cap)->{
            var modifierLevel=modifierEntry.getLevel();
            if (modifierLevel>0){
                var entityList=level.getEntitiesOfClass(Mob.class,livingEntity.getBoundingBox().inflate(8),mob ->{
                    if(mob instanceof Villager)return false;
                    if(mob instanceof TamableAnimal)return false;
                    return !(mob instanceof IronGolem);
                });
                for (Mob mob : entityList) {
                    mob.invulnerableTime=0;
                    mob.hurt(DamageSource.indirectMagic(livingEntity,livingEntity),livingEntity.getMaxHealth() * 0.12f * modifierLevel);
                }
                var size=entityList.size();
                if(size>8){
                    size=8;
                }
                livingEntity.heal((float)(livingEntity.getMaxHealth() * modifierLevel * 0.25f * 0.12f * Math.sqrt(size)));
            }
        });
    }
}
