package com.hoshino.cti.register;

import mekanism.api.chemical.infuse.InfuseType;
import mekanism.common.registration.impl.InfuseTypeDeferredRegister;
import mekanism.common.registration.impl.InfuseTypeRegistryObject;

import static com.hoshino.cti.Cti.MOD_ID;

public class CtiInfusetype {
    public static final InfuseTypeDeferredRegister INFUSE = new InfuseTypeDeferredRegister(MOD_ID);
    public static final InfuseTypeRegistryObject<InfuseType> MANA_INFUSE = INFUSE.register("mana_infuse", 0x65d1ff);
}
