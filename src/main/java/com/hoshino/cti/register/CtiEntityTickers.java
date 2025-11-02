package com.hoshino.cti.register;

import com.hoshino.cti.Cti;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.content.entityTicker.tickers.Emp;
import com.hoshino.cti.content.entityTicker.tickers.EmptyTicker;
import com.hoshino.cti.content.entityTicker.tickers.InvulnerableTicker;
import com.hoshino.cti.content.entityTicker.tickers.Oracle;
import com.hoshino.cti.content.registry.CtiRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CtiEntityTickers {
    public static final DeferredRegister<EntityTicker> ENTITY_TICKERS = DeferredRegister.create(CtiRegistry.ENTITY_TICKER, Cti.MOD_ID);

    public static final RegistryObject<EntityTicker> ORACLE = ENTITY_TICKERS.register("oracle", Oracle::new);
    public static final RegistryObject<EntityTicker> EMP = ENTITY_TICKERS.register("emp", Emp::new);
    public static final RegistryObject<EntityTicker> INVULNERABLE = ENTITY_TICKERS.register("invulnerable", InvulnerableTicker::new);
    public static final RegistryObject<EntityTicker> SACRIFICE_SEAL = ENTITY_TICKERS.register("sacrifice_seal", EmptyTicker::new);
}
