package com.hoshino.cti.content.elementalSystem;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

public class ElementalDamageSource extends EntityDamageSource {
    public boolean isFiery = false;
    public boolean isBurnt = false;
    public boolean isFrozen = false;
    public boolean isChilled = false;
    public boolean isLightning = false;
    public boolean isElectrified = false;
    public boolean isRadioactive = false;
    public boolean isRadiation = false;
    public ElementalDamageSource(String pDamageTypeId, Entity pEntity) {
        super(pDamageTypeId, pEntity);
    }
    public static ElementalDamageSource fiery(Entity entity){
        var source = new ElementalDamageSource(DamageSource.ON_FIRE.msgId,entity);
        source.isFiery = true;
        return source;
    }
    public static ElementalDamageSource burnt(Entity entity){
        var source = new ElementalDamageSource(DamageSource.IN_FIRE.msgId,entity);
        source.isBurnt = true;
        source.bypassArmor();
        return source;
    }
    public static ElementalDamageSource frozen(Entity entity){
        var source = new ElementalDamageSource(DamageSource.FREEZE.msgId,entity);
        source.isFrozen = true;
        return source;
    }
    public static ElementalDamageSource chilled(Entity entity){
        var source = new ElementalDamageSource(DamageSource.FREEZE.msgId,entity);
        source.isChilled = true;
        return source;
    }
    public static ElementalDamageSource lightning(Entity entity){
        var source = new ElementalDamageSource(DamageSource.LIGHTNING_BOLT.msgId,entity);
        source.isLightning = true;
        return source;
    }
    public static ElementalDamageSource electrified(Entity entity){
        var source = new ElementalDamageSource(DamageSource.LIGHTNING_BOLT.msgId,entity);
        source.isElectrified = true;
        return source;
    }
}
