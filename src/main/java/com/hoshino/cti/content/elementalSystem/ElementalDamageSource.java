package com.hoshino.cti.content.elementalSystem;

import lombok.Getter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

public class ElementalDamageSource extends EntityDamageSource {
    @Getter
    protected boolean isFiery = false;
    @Getter
    protected boolean isBurnt = false;
    @Getter
    protected boolean isFrozen = false;
    @Getter
    protected boolean isChilled = false;
    @Getter
    protected boolean isLightning = false;
    @Getter
    protected boolean isElectrified = false;
    @Getter
    protected boolean isRadioactive = false;
    @Getter
    protected boolean isRadiation = false;
    @Getter
    protected boolean isDraconic = false;
    @Getter
    protected boolean isDragon = false;
    @Getter
    protected boolean isChaotic = false;
    @Getter
    protected int chaoticMinDamage = 0;

    public ElementalDamageSource setFiery(){
        this.isFiery = true;
        return this;
    }
    public ElementalDamageSource setBurnt(){
        this.isBurnt = true;
        return this;
    }
    public ElementalDamageSource setFrozen(){
        this.isFrozen = true;
        return this;
    }
    public ElementalDamageSource setChilled(){
        this.isChilled = true;
        return this;
    }
    public ElementalDamageSource setLightning(){
        this.isLightning = true;
        return this;
    }
    public ElementalDamageSource setElectrified(){
        this.isElectrified = true;
        return this;
    }
    public ElementalDamageSource setRadioactive(){
        this.isRadioactive = true;
        return this;
    }
    public ElementalDamageSource setRadiation(){
        this.isRadiation = true;
        return this;
    }
    public ElementalDamageSource setDraconic(){
        this.isDraconic = true;
        return this;
    }
    public ElementalDamageSource setDragon(){
        this.isDragon = true;
        return this;
    }
    public ElementalDamageSource setChaotic(){
        this.isChaotic = true;
        return this;
    }
    public ElementalDamageSource setChaoticMinDamage(int i){
        this.chaoticMinDamage = i;
        return this;
    }

    @Override
    public boolean isBypassArmor() {
        return this.isBurnt()||this.isChilled()||this.isElectrified()||this.isChaotic()||this.isRadiation()||this.isDragon()||super.isBypassArmor();
    }

    public ElementalDamageSource(String pDamageTypeId, Entity pEntity) {
        super(pDamageTypeId, pEntity);
    }
    public static ElementalDamageSource fiery(Entity entity){
        return new ElementalDamageSource("cti.scorch",entity).setFiery();
    }
    public static ElementalDamageSource burnt(Entity entity){
        return new ElementalDamageSource("cti.scorch",entity).setBurnt();
    }
    public static ElementalDamageSource frozen(Entity entity){
        return new ElementalDamageSource(DamageSource.FREEZE.msgId,entity).setFrozen();
    }
    public static ElementalDamageSource chilled(Entity entity){
        return new ElementalDamageSource(DamageSource.FREEZE.msgId,entity).setChilled();
    }
    public static ElementalDamageSource lightning(Entity entity){
        return new ElementalDamageSource(DamageSource.LIGHTNING_BOLT.msgId,entity).setLightning();
    }
    public static ElementalDamageSource electrified(Entity entity){
        return new ElementalDamageSource(DamageSource.LIGHTNING_BOLT.msgId,entity).setElectrified();
    }
    public static ElementalDamageSource chaotic(Entity entity,int minDamage){
        return new ElementalDamageSource("cti.chaotic",entity).setChaotic().setChaoticMinDamage(minDamage);
    }
}
