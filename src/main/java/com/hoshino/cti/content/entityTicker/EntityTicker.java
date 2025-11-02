package com.hoshino.cti.content.entityTicker;

import com.hoshino.cti.content.registry.CtiRegistry;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

//EntityTicker的基础类，你的Ticker需要继承这个
public abstract class EntityTicker {
    private ResourceLocation id = null;
    @Getter
    @Setter
    private boolean infinite = false;
    public ResourceLocation getId(){
        if (this.id==null) this.id = CtiRegistry.ENTITY_TICKER_REGISTRY.getKey(this);
        return this.id;
    }
    public static EntityTicker fromId(ResourceLocation id){
        return CtiRegistry.ENTITY_TICKER_REGISTRY.getValue(id);
    }
    //当实体即将被tick时被调用，返回值表示实体是否被tick（返回false会把实体停住）
    public boolean tick(int duration,int level,Entity entity){
        return true;
    }
    //当EntityTickerInstance通过EntityTickerManagerInstance的方法添加的时候调用
    public void onTickerStart(int duration,int level,Entity entity){

    }
    //当EntityTickerInstance通过EntityTickerManagerInstance的方法移除或者自然结束的时候调用
    public void onTickerEnd(int level,Entity entity){

    }
}
