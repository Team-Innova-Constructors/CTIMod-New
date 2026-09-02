package com.hoshino.cti.util;

import com.hoshino.cti.Modifier.genre.elemental.fiery.Exothermic;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Random;

public class EntityUtil {
    public static boolean isAntiStun(LivingEntity living) {
        ItemStack stack = living.getItemBySlot(EquipmentSlot.HEAD);
        if (stack.getItem() instanceof IModifiable) {
            return ToolStack.from(stack).getModifierLevel(CtiModifiers.anti_stun_goggles.get()) > 0;
        }
        return false;
    }

    public static void homingToward(Entity projectile, Entity target, float strength, float baseRadius){
        double velocity = projectile.getDeltaMovement().length();
        float distance = projectile.distanceTo(target);
        Vec3 movementDirection = projectile.getDeltaMovement().normalize().scale(1.0f/(1+strength));
        Vec3 chasingAccelerate = new Vec3(target.getX()-projectile.getX(), target.getY()+target.getBbHeight()*0.5-projectile.getY()-projectile.getBbHeight()*0.5,target.getZ()-projectile.getZ()).normalize().scale(1+baseRadius/distance);
        Vec3 newMovement = movementDirection.add(chasingAccelerate).normalize().scale(velocity);
        projectile.setDeltaMovement(newMovement);
    }
    public static boolean hasAlGlass(LivingEntity entity) {
        int minX = Mth.floor(entity.getX() - 6);
        int maxX = Mth.floor(entity.getX() + 6);
        int minZ = Mth.floor(entity.getZ() - 6);
        int maxZ = Mth.floor(entity.getZ() + 6);
        int y = Mth.floor(entity.getY() - 1);

        var level = entity.level;
        var targetBlock = CtiBlock.aluminium_glass.get();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                mutablePos.set(x, y, z);
                if (level.getBlockState(mutablePos).getBlock() == targetBlock) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void constantKill(LivingEntity entity, DamageSource source){
        var a = (ILivingEntityMixin) entity;
        entity.invulnerableTime=0;
        var currentHealth=entity.getMaxHealth();
        if(entity.getHealth()<=2)return;
        entity.setHealth(0);
        entity.die(source);
    }

    public static int inflictBurnt(Player cause, LivingEntity target, @Nullable IToolStackView tool,float totalBurnt){
        int maxValue = (int) cause.getAttributeValue(CtiAttributes.BURNT_INFLICT.get());
        if (tool!=null&&totalBurnt>0){
            if (Exothermic.getFuelTemp(tool,cause)>0){
                totalBurnt+=totalBurnt*Exothermic.getFuelTemp(tool,cause)/4000f;
            }
        }
        int toInflict = (int) Math.floor(totalBurnt);
        float extraChance = totalBurnt-toInflict;
        var random = new Random();
        if (random.nextFloat()<=extraChance) toInflict++;

        if (maxValue<=0||toInflict<=0) return 0;
        var playerId = cause.getUUID();
        var instance = EntityTickerManager.getInstancePlayerSpecific(target,playerId);
        var tickerInstance = instance.getTicker(CtiEntityTickers.FIERY.get());
        int originalValue = tickerInstance!=null ? tickerInstance.level : 0;
        instance.addTicker(new EntityTickerInstance(CtiEntityTickers.FIERY.get(), toInflict,600),
                (i1,i2)-> Math.min(i1+i2,maxValue),
                (i1,i2)-> i1>0?i1:i2);
        tickerInstance = instance.getTicker(CtiEntityTickers.FIERY.get());
        int finalValue = tickerInstance!=null ? tickerInstance.level : 0;
        return finalValue-originalValue;
    }
}
