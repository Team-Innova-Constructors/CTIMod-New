package com.hoshino.cti.content.environmentSystem;

import com.hoshino.cti.register.CtiAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class EDamageSource extends DamageSource implements IEnvironmentalSource {
    public final boolean fromBiomes;
    public final int level;
    public final Attribute resistAttribute;
    public EDamageSource(String pMessageId, Attribute resistAttribute, boolean fromBiomes, int level) {
        super(pMessageId);
        this.fromBiomes = fromBiomes;
        this.level=level;
        this.resistAttribute = resistAttribute;
    }

    public static EDamageSource scorched(boolean fromBiomes,int level){
        return new EDamageSource("cti.scorch", CtiAttributes.SCORCHED_RESISTANCE.get(),fromBiomes,level);
    }
    public static EntitySource indirectScorched(boolean fromBiomes,Entity entity,int level){
        return new EntitySource("cti.scorch", CtiAttributes.SCORCHED_RESISTANCE.get(),entity,fromBiomes,level);
    }

    public static EDamageSource frozen(boolean fromBiomes,int level){
        return new EDamageSource("cti.frozen", CtiAttributes.FROZEN_RESISTANCE.get(),fromBiomes,level);
    }
    public static EntitySource indirectFrozen(boolean fromBiomes,Entity entity,int level){
        return new EntitySource("cti.frozen", CtiAttributes.FROZEN_RESISTANCE.get(),entity,fromBiomes,level);
    }

    public static EDamageSource pressure(boolean fromBiomes,int level){
        return new EDamageSource("cti.pressure", CtiAttributes.PRESSURE_RESISTANCE.get(),fromBiomes,level);
    }
    public static EntitySource indirectPressure(boolean fromBiomes,Entity entity,int level){
        return new EntitySource("cti.pressure", CtiAttributes.PRESSURE_RESISTANCE.get(),entity,fromBiomes,level);
    }

    public static EDamageSource ionize(boolean fromBiomes,int level){
        return new EDamageSource("cti.ionized", CtiAttributes.IONIZE_RESISTANCE.get(),fromBiomes,level);
    }
    public static EntitySource indirectIonize(boolean fromBiomes,Entity entity,int level){
        return new EntitySource("cti.ionized", CtiAttributes.IONIZE_RESISTANCE.get(),entity,fromBiomes,level);
    }

    @Override
    public Attribute getResistAttribute() {
        return this.resistAttribute;
    }

    @Override
    public boolean fromBiomes() {
        return this.fromBiomes;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    public static class EntitySource extends EntityDamageSource implements IEnvironmentalSource {
        public final boolean fromBiomes;
        public final int level;
        public final Attribute resistAttribute;
        public EntitySource(String pDamageTypeId, Attribute resistAttribute, Entity pEntity,boolean fromBiomes,int level) {
            super(pDamageTypeId, pEntity);
            this.fromBiomes = fromBiomes;
            this.level = level;
            this.resistAttribute = resistAttribute;
        }

        @Override
        public boolean fromBiomes() {
            return this.fromBiomes;
        }
        @Override
        public int getLevel() {
            return this.level;
        }
        @Override
        public Attribute getResistAttribute() {
            return this.resistAttribute;
        }
    }
}
