package com.hoshino.cti.register;

import com.hoshino.cti.Effects.*;
import com.marth7th.solidarytinker.effects.StaticEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

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
    public static final RegistryObject<MobEffect> solid = EFFECT.register("solid", Solid::new);
    public static final RegistryObject<MobEffect> solid_armor = EFFECT.register("solid_armor", SolidArmor::new);
    public static final RegistryObject<MobEffect> heng = EFFECT.register("heng",Heng::new);
    public static final RegistryObject<MobEffect> ha = EFFECT.register("ha",Ha::new);
    public static final RegistryObject<MobEffect> strong = EFFECT.register("strong",Strong::new);
    public static final RegistryObject<MobEffect> covert = EFFECT.register("covert",Covert::new);
    public static final RegistryObject<MobEffect> nakshatra = EFFECT.register("nakshatra",()-> new StaticEffect(MobEffectCategory.BENEFICIAL,0xad0101).addAttributeModifier(Attributes.MAX_HEALTH, UUID.nameUUIDFromBytes("ank".getBytes()).toString(),-0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> blood_angry = EFFECT.register("blood_angry", BloodAngry::new);
    public static final RegistryObject<MobEffect> INSATIABLE_BOOST = EFFECT.register("insatiable_boost",()-> new StaticEffect(MobEffectCategory.BENEFICIAL,0x9546C9).addAttributeModifier(
            CtiAttributes.MAX_INSATIABLE.get(),"f74c192d-4d5c-e9e6-a8ea-c7db8f8280b5",4, AttributeModifier.Operation.ADDITION
    ));
    public static final RegistryObject<MobEffect> OVERHEAT = EFFECT.register("overheat",()->new StaticEffect(MobEffectCategory.BENEFICIAL,0xFF761B));
    public static final RegistryObject<MobEffect> OXIDIZE_FLAME = EFFECT.register("oxidize_flame",()-> new StaticEffect(MobEffectCategory.BENEFICIAL,0xFFC172)
            .addAttributeModifier(CtiAttributes.BURNT_ATTACK_SPEED.get(),"174a4bee-f1f6-01c4-6df1-8151b7e10952",0.2, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> NARROW_ESCAPE = EFFECT.register("narrow_escape",()-> new StaticEffect(MobEffectCategory.BENEFICIAL,0xBC57FF)
            .addAttributeModifier(CtiAttributes.BURNT_ATTACK_SPEED.get(),"a3134ce1-0036-bb68-ccaa-2a5f6b703cb7",0.2, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(CtiAttributes.BURNT_INFLICT.get(),"bc11b1bc-5ac9-96f6-9059-7c28e8b8c7b8",0.2, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(CtiAttributes.MAX_INSATIABLE.get(),"30c48b90-d3e0-323d-22e9-6b489f9d8207",0.2, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE,"26e48275-22c4-5409-e326-3a21c7537863",0.2, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED,"557c40a1-bdea-2685-5663-e29094b1019c",0.2, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(Attributes.LUCK,"1fa0affb-8297-ad42-71aa-5923ab7eb0d8",5, AttributeModifier.Operation.ADDITION)
    );
}
