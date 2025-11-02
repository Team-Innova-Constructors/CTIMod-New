package com.hoshino.cti.register;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.attribute.GasAttributes;
import mekanism.api.math.FloatingLong;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

import static com.hoshino.cti.Cti.MOD_ID;

public class CtiChemical {
    public static final GasDeferredRegister GAS = new GasDeferredRegister(MOD_ID);

    public static final GasRegistryObject<Gas> MANA_GAS = GAS.register("mana_gas", 0x65d1ff);
    public static final GasRegistryObject<Gas> NEUTRON = GAS.register("neutron", 0x404259);
    public static final GasRegistryObject<Gas> OVERCHARGED_NEUTRON = GAS.register("overcharged_neutron", 0x6A00DD);
    public static final GasRegistryObject<Gas> COLD_SODIUM = GAS.register("cold_sodium", 0x0000FF);
    public static final GasRegistryObject<Gas> FISSION_PLASMA = GAS.register("fission_plasma", 0xFFC1C2);
    public static final GasRegistryObject<Gas> METHANE = GAS.register("methane", 0xD5FDFF,new GasAttributes.Fuel(()->30,()-> FloatingLong.create(20480)));
    public static final GasRegistryObject<Gas> ETHANOL = GAS.register("ethanol", 0xA7E6B1,new GasAttributes.Fuel(()->60,()-> FloatingLong.create(16384)));
    public static final GasRegistryObject<Gas> ALDEHYDE = GAS.register("aldehyde", 0xCFE6B9);
    public static final GasRegistryObject<Gas> PHENOL = GAS.register("phenol", 0xE6C8A5);
    public static final GasRegistryObject<Gas> PHENOLIC = GAS.register("phenolic_resin", 0xB3774C);
    public static final GasRegistryObject<Gas> BENZOIC_ACID = GAS.register("benzoic_acid", 0xCFC869);
    public static final GasRegistryObject<Gas> BPO = GAS.register("dibenzoyl_peroxide", 0xAD5637);
    public static final GasRegistryObject<Gas> CHROMATIC_METAL = GAS.register("chromatic_metal", 0xF8B3FF);
    public static final GasRegistryObject<Gas> POLYMER = GAS.register("polymer", 0x9AA1C7);
    public static final GasRegistryObject<Gas> PRECURSOR = GAS.register("precursor", 0xFFFD71);
    public static final GasRegistryObject<Gas> PURE_MATTER = GAS.register("pure_matter", 0x000000);
    public static final GasRegistryObject<Gas> CONCENTRATED_CARBON = GAS.register("concentrated_carbon", 0xFF5943,new GasAttributes.Fuel(()->100,()-> FloatingLong.create(131072)));
}
