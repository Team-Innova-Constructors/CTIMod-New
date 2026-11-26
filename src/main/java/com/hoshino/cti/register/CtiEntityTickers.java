package com.hoshino.cti.register;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Modifier.DoubleEdged;
import com.hoshino.cti.content.entityTicker.tickers.EnderStiction;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.content.entityTicker.tickers.*;
import com.hoshino.cti.content.registry.CtiRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CtiEntityTickers {
    public static final DeferredRegister<EntityTicker> ENTITY_TICKERS = DeferredRegister.create(CtiRegistry.ENTITY_TICKER, Cti.MOD_ID);

    public static final RegistryObject<EntityTicker> ORACLE = ENTITY_TICKERS.register("oracle", Oracle::new);
    public static final RegistryObject<EntityTicker> EMP = ENTITY_TICKERS.register("emp", Emp::new);
    public static final RegistryObject<EntityTicker> INVULNERABLE = ENTITY_TICKERS.register("invulnerable", InvulnerableTicker::new);
    public static final RegistryObject<EntityTicker> SACRIFICE_SEAL = ENTITY_TICKERS.register("sacrifice_seal",()-> new EmptyTicker(MobEffectCategory.HARMFUL));
    public static final RegistryObject<EntityTicker> FIERY = ENTITY_TICKERS.register("fiery", Fiery::new);
    public static final RegistryObject<EntityTicker> VULNERABLE = ENTITY_TICKERS.register("vulnerable", VulnerableTicker::new);
    public static final RegistryObject<EntityTicker> SOUL = ENTITY_TICKERS.register("soul", Soul::new);
    public static final RegistryObject<EntityTicker> ENDER_STICTION = ENTITY_TICKERS.register("ender_stiction", EnderStiction::new);
    public static final RegistryObject<EntityTicker> STRICT_CURSE = ENTITY_TICKERS.register("strict_curse", StrictCurse::new);
    public static final RegistryObject<EntityTicker> NIGHTMARE = ENTITY_TICKERS.register("nightmare", EmptyTicker::new);
    public static final RegistryObject<EntityTicker> ENDER_SUPPRESS = ENTITY_TICKERS.register("ender_suppress", EnderSuppress::new);
    public static final RegistryObject<EntityTicker> DAWN = ENTITY_TICKERS.register("dawn", Dawn::new);
    public static final RegistryObject<EntityTicker> DAWN_EXTRA_DAMAGE = ENTITY_TICKERS.register("dawn_extra_damage", EmptyTicker::new);
    public static final RegistryObject<EntityTicker> ECHO_FORM = ENTITY_TICKERS.register("echo_form", EchoFormTicker::new);
    public static final RegistryObject<EntityTicker> FIERY_TOUCH_CD = ENTITY_TICKERS.register("fiery_touch_cd", EmptyTicker::new);
    public static final RegistryObject<EntityTicker> RUBY_LASER_CD = ENTITY_TICKERS.register("ruby_laser_cd", EmptyTicker::new);
    public static final RegistryObject<EntityTicker> REFLECT_TICKER = ENTITY_TICKERS.register("reflect_ticker", ReflectTicker::new);
    public static final RegistryObject<EntityTicker> BIREFRINGENT_TICKER = ENTITY_TICKERS.register("birefringent_ticker", EchoFormTicker::new);
    public static final RegistryObject<EntityTicker> ATOMIC_DECOMPOSE = ENTITY_TICKERS.register("atomic_decompose", AtomicDecompose::new);
    public static final RegistryObject<EntityTicker> DOUBLE_EDGED = ENTITY_TICKERS.register("double_edged", EmptyTicker::new);
    public static final RegistryObject<EntityTicker> DOUBLE_EDGED_RECOVERY = ENTITY_TICKERS.register("double_edged_recovery", DoubleEdgedRecovery::new);
    public static final RegistryObject<EntityTicker> WARP_ENGINE = ENTITY_TICKERS.register("warp_engine", WarpEngineTicker::new);
    public static final RegistryObject<EntityTicker> STELLAR_FIERY = ENTITY_TICKERS.register("stellar_fiery", EmptyTicker::new);
    public static final RegistryObject<EntityTicker> CORONATION_BURNT = ENTITY_TICKERS.register("coronation_burnt",()-> new EmptyTicker(MobEffectCategory.BENEFICIAL));
    public static final RegistryObject<EntityTicker> DRAGON_DISORDER = ENTITY_TICKERS.register("dragon_disorder", DragenDisorder::new);
    public static final RegistryObject<EntityTicker> OVERHEAT_DELAY = ENTITY_TICKERS.register("overheat_delay", OverHeatDelay::new);
    public static final RegistryObject<EntityTicker> EJIAN = ENTITY_TICKERS.register("ejian", Ejian::new);

}
