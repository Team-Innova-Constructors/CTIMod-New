package com.hoshino.cti.register;

import com.hoshino.cti.Effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hoshino.cti.Cti.MOD_ID;

public class CtiEffects {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final RegistryObject<MobEffect> stress = EFFECT.register("stress", Stress::new);
    public static final RegistryObject<MobEffect> ev = EFFECT.register("ev", Ev::new);
    public static final RegistryObject<MobEffect> Abel = EFFECT.register("abel", Abel::new);
    public static final RegistryObject<MobEffect> resolute = EFFECT.register("resolute", Resolute::new);
    public static final RegistryObject<MobEffect> AncientDragonFlame = EFFECT.register("ancientdragonflame", AncientDragonFlame::new);
    public static final RegistryObject<MobEffect> meteor_shower = EFFECT.register("meteor_shower", MeteorShower::new);
    public static final RegistryObject<MobEffect> numerical_perception = EFFECT.register("numerical_perception", NumericalPerception::new);
    public static final RegistryObject<MobEffect> curve_mapping = EFFECT.register("curve_mapping", CurveMapping::new);
    public static final RegistryObject<MobEffect> supplementary_formula = EFFECT.register("supplementary_formula", SupplementaryFormula::new);
    public static final RegistryObject<MobEffect> heng = EFFECT.register("heng",Heng::new);
    public static final RegistryObject<MobEffect> ha = EFFECT.register("ha",Ha::new);
    public static final RegistryObject<MobEffect> strong = EFFECT.register("strong",Strong::new);
    public static final RegistryObject<MobEffect> covert = EFFECT.register("covert",Covert::new);
    public static final RegistryObject<MobEffect> nakshatra = EFFECT.register("nakshatra",Nakshatra::new);

}
