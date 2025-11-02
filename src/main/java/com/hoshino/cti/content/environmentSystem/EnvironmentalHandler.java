package com.hoshino.cti.content.environmentSystem;

import cofh.core.init.CoreParticles;
import com.c2h6s.etshtinker.init.etshtinkerParticleType;
import com.hoshino.cti.Capabilitiess.*;
import com.hoshino.cti.util.ILivingEntityMixin;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PFrozenValueSync;
import com.hoshino.cti.netwrok.packet.PIonizeValueSync;
import com.hoshino.cti.netwrok.packet.PPressureValueSync;
import com.hoshino.cti.netwrok.packet.PScorchValueSync;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.util.CtiTagkey;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.hoshino.cti.util.BiomeUtil.*;
import static com.hoshino.cti.util.BiomeUtil.getBiomePressureLevel;

public class EnvironmentalHandler {
    public static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    //四种环境危害的CompoundTag键名
    public static final String IONIZE_AMOUNT = "environmental.ionize";
    public static final String SCORCH_AMOUNT = "environmental.scorch";
    public static final String FROZEN_AMOUNT = "environmental.frozen";
    public static final String PRESSURE_AMOUNT = "environmental.pressure";

    //直接伤害的危害基础伤害
    public static final float baseDamage = 10;
    //百分比伤害的危害基础百分比
    public static final float baseMultiplier = 0.05f;


    //为了避免环境伤害被减免，使用自定义的伤害方法
    public static boolean hurtEntity(LivingEntity living, DamageSource source, float amount){
        if (source instanceof IEnvironmentalSource environmental){
            if (environmental.fromBiomes()) return ((ILivingEntityMixin) living).cti$strictHurt(source,amount);
            else {
                double protection = 0;
                AttributeInstance instance = living.getAttribute(environmental.getResistAttribute());
                if (instance!=null){
                    protection = instance.getValue();
                }
                double multiplier =Math.max(0,environmental.getLevel()-protection) ;
                return ((ILivingEntityMixin) living).cti$strictHurt(source, (float) (amount*multiplier));
            }
        }
        return false;
    }

    //环境刻，每10tick运行一次
    public static void livingTick(LivingEntity living){
        if (living.getLevel() instanceof ServerLevel level) {
            Holder<Biome> biome = level.getBiome(living.blockPosition());
            //危害水平，等于群系水平-防护水平，后续可以补充
            float ionizeResistance = getIonizeResistance(living);
            float scorchResistance = getScorchResistance(living);
            float frozenResistance = getFrozenResistance(living);
            float pressureResistance = getPressureResistance(living);

            float ionizeLevel = getBiomeIonizeLevel(biome) - ionizeResistance;
            float scorchLevel = getBiomeScorchLevel(biome) - scorchResistance - getBiomeFreezeLevel(biome);
            float frozenLevel = getBiomeFreezeLevel(biome) - frozenResistance - getBiomeScorchLevel(biome);
            float pressureLevel = getBiomePressureLevel(biome) - pressureResistance;
            if (living.getMaxHealth() > 5000 && !(living instanceof Player)) {
                ionizeLevel = -2F;
                scorchLevel = -2F;
                frozenLevel = -2F;
                pressureLevel = -2F;
            }
            if (living.isAlive()){
                int value = getScorchValue(living);
                int volume = getEnvironmentalVolume(living);
                float baseBuildUp = volume/100f;
                int maxValue = (int)(scorchLevel *4)* getEnvironmentalVolume(living)/4;
                float baseDecrease = volume/100f;
                float toAdd = 0;
                if (scorchLevel >0&& value <maxValue){
                    toAdd = Math.max(1,baseBuildUp* scorchLevel);
                    int added =(int) Math.min(maxValue- value,toAdd);
                    addScorchValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PScorchValueSync((float) (value * 100) /volume, toAdd*100/volume), player);
                    }
                }else if (scorchLevel <=0&& value >0){
                    toAdd = Math.min(-1, baseDecrease*scorchLevel);
                    int added =(int) Math.max(-value,toAdd);
                    addScorchValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PScorchValueSync((float) (value * 100) /volume, toAdd*100/volume), player);
                    }
                } else {
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PScorchValueSync((float) (value * 100) /volume, toAdd*100/volume), player);
                    }
                }

                toAdd =0;

                value = getFrozenValue(living);
                maxValue = (int)(frozenLevel *4)*volume/4;
                if (frozenLevel >0&& value <maxValue){
                    toAdd = Math.max(1,baseBuildUp* frozenLevel);
                    int added =(int) Math.min(maxValue- value,toAdd);
                    addFrozenValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PFrozenValueSync((float) (value * 100) /volume, toAdd *100/volume), player);
                    }
                }else if (frozenLevel <=0&& value >0){
                    toAdd = Math.min(-1, baseDecrease*frozenLevel);
                    int added =(int) Math.max(-value,toAdd);
                    addFrozenValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PFrozenValueSync((float) (value * 100) /volume, toAdd *100/volume), player);
                    }
                } else {
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PFrozenValueSync((float) (value * 100) /volume, toAdd *100/volume), player);
                    }
                }

                toAdd=0;

                value = getPressureValue(living);
                maxValue = (int)(frozenLevel *4)*volume/4;
                if (pressureLevel >0&& value <maxValue){
                    toAdd = Math.max(1,baseBuildUp* pressureLevel);
                    int added =(int) Math.min(maxValue- value,toAdd);
                    addPressureValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PPressureValueSync((float) (value * 100) /volume, toAdd *100/volume), player);
                    }
                }else if (pressureLevel <=0&& value >0){
                    toAdd = Math.min(-1, baseDecrease*pressureLevel);
                    int added =(int) Math.max(-value,toAdd);
                    addPressureValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PPressureValueSync((float) (value * 100) /volume, toAdd *100/volume), player);
                    }
                } else {
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PPressureValueSync((float) (value * 100) /volume, toAdd *100/volume), player);
                    }
                }

                toAdd=0;

                value = getIonizeValue(living);
                maxValue = (int)(ionizeLevel *4)*volume/4;
                if (ionizeLevel >0&& value <maxValue){
                    toAdd = Math.max(1,baseBuildUp* ionizeLevel);
                    int added =(int) Math.min(maxValue- value,Math.max(1,baseBuildUp* ionizeLevel));
                    addIonizeValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PIonizeValueSync((float) (value * 100) /volume, toAdd*100/volume), player);
                    }
                }else if (ionizeLevel <=0&& value >0){
                    toAdd = Math.min(-1, baseDecrease*ionizeLevel);
                    int added =(int) Math.max(-value,Math.min(-1, baseDecrease*ionizeLevel));
                    addIonizeValue(living,added);
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PIonizeValueSync((float) (value * 100) /volume, toAdd*100/volume), player);
                    }
                } else {
                    if (living instanceof ServerPlayer player) {
                        CtiPacketHandler.sendToPlayer(new PIonizeValueSync((float) (value * 100) /volume, toAdd*100/volume), player);
                    }
                }



                //压强的大伤害
                if (pressureLevel>=1.5){
                    hurtEntity(living,EDamageSource.pressure(true,10),Float.MAX_VALUE);
                }

                float harmLevel = (float) getScorchValue(living)/getEnvironmentalVolume(living);
                if (harmLevel>0.5){
                    hurtEntity(living,EDamageSource.scorched(true,(int) harmLevel),harmLevel*baseDamage);
                    postScorchEffect(harmLevel,living);
                    if (living.level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.FLAME, living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), (int) (harmLevel * 10), 0, 0, 0, 0.25);
                    }
                }
                harmLevel = (float) getFrozenValue(living)/getEnvironmentalVolume(living);
                if (harmLevel>0.25){
                    hurtEntity(living,EDamageSource.frozen(true,(int) harmLevel),harmLevel*baseDamage*0.5f);
                    postFrozenEffect(harmLevel,living);
                    if (living.level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(CoreParticles.FROST.get(), living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), (int) (harmLevel * 10), 0, 0, 0, 0.25);
                    }
                }
                harmLevel = (float) getPressureValue(living)/getEnvironmentalVolume(living);
                if (harmLevel>0.1){
                    hurtEntity(living,EDamageSource.pressure(true,(int) harmLevel),harmLevel*baseMultiplier*5*living.getMaxHealth());
                    postPressureEffect(harmLevel,living);
                    if (living.level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.SMOKE, living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), (int) (harmLevel * 10), 0, 0, 0, 0.25);
                    }
                }
                harmLevel = (float) getIonizeValue(living)/getEnvironmentalVolume(living);
                if (harmLevel>0.1){
                    hurtEntity(living,EDamageSource.ionize(true,(int) harmLevel),harmLevel*baseMultiplier*living.getMaxHealth());
                    postIonizeEffect(harmLevel,living);
                    if (living.level instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(etshtinkerParticleType.electric.get(), living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), (int) (harmLevel * 10), 0, 0, 0, 0.25);
                    }
                }
            }
        }
    }

    public static void postScorchEffect(float harmLevel,LivingEntity living){
        living.setSecondsOnFire((int) (20*harmLevel));
    }
    public static void postFrozenEffect(float harmLevel,LivingEntity living){
        living.setTicksFrozen((int) (20*harmLevel));
        living.wasInPowderSnow=true;
    }
    public static void postPressureEffect(float harmLevel,LivingEntity living){

    }
    public static void postIonizeEffect(float harmLevel,LivingEntity living){

    }

    public static void updateEnvironmentalAttribute(LivingEntity living){

    }


    public static void addScorchValue(@NotNull LivingEntity living, int amount) {
        CompoundTag nbt = living.getPersistentData();
        nbt.putInt(SCORCH_AMOUNT, nbt.getInt(SCORCH_AMOUNT) + amount);
    }
    public static void addFrozenValue(@NotNull LivingEntity living, int amount) {
        CompoundTag nbt = living.getPersistentData();
        nbt.putInt(FROZEN_AMOUNT, nbt.getInt(FROZEN_AMOUNT) + amount);
    }
    public static void addIonizeValue(@NotNull LivingEntity living, int amount) {
        CompoundTag nbt = living.getPersistentData();
        nbt.putInt(IONIZE_AMOUNT, nbt.getInt(IONIZE_AMOUNT) + amount);
    }
    public static void addPressureValue(@NotNull LivingEntity living, int amount) {
        CompoundTag nbt = living.getPersistentData();
        nbt.putInt(PRESSURE_AMOUNT, nbt.getInt(PRESSURE_AMOUNT) + amount);
    }

    public static int getScorchValue(@NotNull LivingEntity living) {
        return living.getPersistentData().getInt(SCORCH_AMOUNT);
    }
    public static int getFrozenValue(@NotNull LivingEntity living) {
        return living.getPersistentData().getInt(FROZEN_AMOUNT);
    }
    public static int getIonizeValue(@NotNull LivingEntity living) {
        return living.getPersistentData().getInt(IONIZE_AMOUNT);
    }
    public static int getPressureValue(@NotNull LivingEntity living) {
        return living.getPersistentData().getInt(PRESSURE_AMOUNT);
    }
    public static boolean isEnvironmentalSafe(@NotNull LivingEntity living){
        return getScorchValue(living)<0&&getFrozenValue(living)<0&&getIonizeValue(living)<0&&getPressureValue(living)<0;
    }

    public static int getEnvironmentalVolume(@NotNull LivingEntity living) {
        return 100;
    }

    public static float getIonizeResistance(LivingEntity living) {
        float resist = 0;
        AttributeInstance instance = living.getAttribute(CtiAttributes.IONIZE_RESISTANCE.get());
        if (instance!=null) {
            resist += (float) instance.getValue();
        }
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = living.getItemBySlot(slot);
            Optional<IElectricShielding> shielding = getCapability(stack, ctiCapabilities.ELECTRIC_SHIELDING, null).resolve();
            if (shielding.isPresent()) {
                resist += shielding.get().getElectricShieldinng();
            } else if (stack.getTags().toList().contains(CtiTagkey.ENVIRONMENT_ADV)) {
                resist += 10f;
            }
        }
        return resist;
    }

    public static float getScorchResistance(LivingEntity living) {
        float resist = 0;
        AttributeInstance instance = living.getAttribute(CtiAttributes.SCORCHED_RESISTANCE.get());
        if (instance!=null) {
            resist += (float) instance.getValue();
        }
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = living.getItemBySlot(slot);
            Optional<IScorchShielding> shielding = getCapability(stack, ctiCapabilities.SCORCH_SHIELDING, null).resolve();
            if (shielding.isPresent()) {
                resist += shielding.get().getScorchShieldinng();
            } else if (stack.getTags().toList().contains(CtiTagkey.PRESSURE_MINOR)) {
                resist += 1f;
            } else if (stack.getTags().toList().contains(CtiTagkey.ENVIRONMENT_ADV)) {
                resist += 10f;
            }
        }
        return resist;
    }

    public static float getFrozenResistance(LivingEntity living) {
        float resist = 0;
        AttributeInstance instance = living.getAttribute(CtiAttributes.FROZEN_RESISTANCE.get());
        if (instance!=null) {
            resist += (float) instance.getValue();
        }
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = living.getItemBySlot(slot);
            Optional<IFreezeShielding> shielding = getCapability(stack, ctiCapabilities.FREEZE_SHIELDING, null).resolve();
            if (shielding.isPresent()) {
                resist += shielding.get().getFreezeShieldinng();
            } else if (stack.getTags().toList().contains(CtiTagkey.PRESSURE_MINOR)) {
                resist += 0.5f;
            } else if (stack.getTags().toList().contains(CtiTagkey.ENVIRONMENT_ADV)) {
                resist += 10f;
            }
        }
        return resist;
    }

    public static float getPressureResistance(LivingEntity living) {
        float resist = 0;
        AttributeInstance instance = living.getAttribute(CtiAttributes.PRESSURE_RESISTANCE.get());
        if (instance!=null) {
            resist += (float) instance.getValue();
        }
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = living.getItemBySlot(slot);
            Optional<IPressureShielding> shielding = getCapability(stack, ctiCapabilities.PRESSURE_SHIELDING, null).resolve();
            if (shielding.isPresent()) {
                resist += shielding.get().getPressureShielding();
            } else if (stack.getTags().toList().contains(CtiTagkey.PRESSURE_MINOR)) {
                resist += 0.5f;
            } else if (stack.getTags().toList().contains(CtiTagkey.ENVIRONMENT_ADV)) {
                resist += 10f;
            }
        }
        return resist;
    }
    @NotNull
    public static <T> LazyOptional<T> getCapability(@Nullable ICapabilityProvider provider, @Nullable Capability<T> cap, @Nullable Direction side) {
        if (provider == null || cap == null || !cap.isRegistered()) {
            return LazyOptional.empty();
        }
        return provider.getCapability(cap, side);
    }
}
