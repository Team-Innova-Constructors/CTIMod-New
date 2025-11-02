package com.hoshino.cti.register;

import com.hoshino.cti.Cti;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CtiAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Cti.MOD_ID);

    public static final RegistryObject<Attribute> SCORCHED_RESISTANCE = ATTRIBUTES.register("scorch_resistance",()->new RangedAttribute("attribute.cti.scorch_resistance",0,-1024,1024D).setSyncable(true));
    public static final RegistryObject<Attribute> FROZEN_RESISTANCE = ATTRIBUTES.register("frozen_resistance",()->new RangedAttribute("attribute.cti.frozen_resistance",0,-1024,1024D).setSyncable(true));
    public static final RegistryObject<Attribute> PRESSURE_RESISTANCE = ATTRIBUTES.register("pressure_resistance",()->new RangedAttribute("attribute.cti.pressure_resistance",0,-1024,1024D).setSyncable(true));
    public static final RegistryObject<Attribute> IONIZE_RESISTANCE = ATTRIBUTES.register("ionize_resistance",()->new RangedAttribute("attribute.cti.ionize_resistance",0,-1024,1024D).setSyncable(true));
}
