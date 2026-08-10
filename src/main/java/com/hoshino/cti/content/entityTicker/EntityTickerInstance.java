package com.hoshino.cti.content.entityTicker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.UUID;

//用法类似于MobEffectInstance，用于存储持续时间和等级。
//如果你觉得时间和等级两个性质不够用可以继承后写个新的。
public class EntityTickerInstance {
    public final EntityTicker ticker;
    public int duration;
    public int level;
    @Nullable
    public final UUID playerCauseId;
    public EntityTickerInstance(EntityTicker ticker, int level, int duration, @Nullable UUID playerCauseId){
        this.ticker = ticker;
        this.level = level;
        this.duration =duration;
        this.playerCauseId = playerCauseId;
    }
    public EntityTickerInstance(EntityTicker ticker, int level, int duration){
        this(ticker,level,duration,null);
    }
    public static EntityTickerInstance readFromNbt(CompoundTag nbt,ResourceLocation name){
        EntityTicker ticker1 = EntityTicker.fromId(name);
        if (ticker1!=null) return new EntityTickerInstance(ticker1,nbt.getInt("level"),nbt.getInt("duration"));
        else return null;
    }
    public static EntityTickerInstance readFromNbtPlayerSpecific(CompoundTag nbt,ResourceLocation name,UUID playerUUID){
        EntityTicker ticker1 = EntityTicker.fromId(name);
        if (ticker1!=null) return new EntityTickerInstance(ticker1,nbt.getInt("level"),nbt.getInt("duration"),playerUUID);
        else return null;
    }
    public void writeToNbt(CompoundTag nbt){
        CompoundTag tag = new CompoundTag();
        tag.putInt("duration",this.duration);
        tag.putInt("level",this.level);
        nbt.put(this.ticker.getId().toString(),tag);
    }
}
