package com.hoshino.cti.register;

import com.hoshino.cti.Cti;
import com.hoshino.cti.content.entityTicker.EnderStiction;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.content.entityTicker.tickers.*;
import com.hoshino.cti.content.registry.CtiRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CtiEntityTickers {
    public static final DeferredRegister<EntityTicker> ENTITY_TICKERS = DeferredRegister.create(CtiRegistry.ENTITY_TICKER, Cti.MOD_ID);

    public static final RegistryObject<EntityTicker> ORACLE = ENTITY_TICKERS.register("oracle", Oracle::new);
    public static final RegistryObject<EntityTicker> EMP = ENTITY_TICKERS.register("emp", Emp::new);
    public static final RegistryObject<EntityTicker> INVULNERABLE = ENTITY_TICKERS.register("invulnerable", InvulnerableTicker::new);
    public static final RegistryObject<EntityTicker> SACRIFICE_SEAL = ENTITY_TICKERS.register("sacrifice_seal", EmptyTicker::new);
    public static final RegistryObject<EntityTicker> FIERY = ENTITY_TICKERS.register("fiery", Fiery::new);
    public static final RegistryObject<EntityTicker> VULNERABLE = ENTITY_TICKERS.register("vulnerable", VulnerableTicker::new);
    public static final RegistryObject<EntityTicker> SOUL = ENTITY_TICKERS.register("soul", Soul::new);
    public static final RegistryObject<EntityTicker> ENDER_STICTION = ENTITY_TICKERS.register("ender_stiction", EnderStiction::new);
    public static final RegistryObject<EntityTicker> STRICT_CURSE = ENTITY_TICKERS.register("strict_curse", StrictCurse::new);
}
