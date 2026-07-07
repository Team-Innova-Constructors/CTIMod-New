package com.hoshino.cti.register;

import com.hollingsworth.arsnouveau.setup.APIRegistry;
import com.hoshino.cti.integration.ArsNouveau.ElementalSummonRitual;
import com.hoshino.cti.integration.ArsNouveau.MeteorShowerRitual;

public class CtiRitual {
    public static void init() {
        APIRegistry.registerRitual(new MeteorShowerRitual());
        APIRegistry.registerRitual(new ElementalSummonRitual());
    }
}
