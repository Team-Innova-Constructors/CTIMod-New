package com.hoshino.cti.Capabilitiess;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ctiCapabilities {
    //抗性（后续可补充）
    public static final Capability<IElectricShielding> ELECTRIC_SHIELDING = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IScorchShielding> SCORCH_SHIELDING = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IFreezeShielding> FREEZE_SHIELDING = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IPressureShielding> PRESSURE_SHIELDING = CapabilityManager.get(new CapabilityToken<>() {
    });
}
