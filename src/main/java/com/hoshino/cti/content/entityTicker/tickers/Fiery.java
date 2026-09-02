package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.Modifier.genre.elemental.fiery.Exothermic;
import com.hoshino.cti.Modifier.genre.elemental.fiery.ReplacedHeatSword;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiModifiers;
import flaxbeard.immersivepetroleum.client.particle.IPParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Random;

public class Fiery extends EntityTicker {
    public Fiery() {
        super(MobEffectCategory.HARMFUL);
    }

    @Override
    public boolean tick(int duration, int level, Entity entity) {
        return true;
    }

    @Override
    public boolean tickPlayerSpecific(int duration, int level, Entity entity, Player player) {
        var attackSpeed = player.getAttributeValue(CtiAttributes.BURNT_ATTACK_SPEED.get());
        int spacing = (int) Math.ceil(20/attackSpeed);
        if (duration%spacing==0){
            burntDamage(entity,player,level);
        }
        return true;
    }

    public static void burntDamage(Entity entity,Player player,int level){
        Random random = new Random();
        int inv = entity.invulnerableTime;
        var mainHandStack = player.getMainHandItem();
        if (mainHandStack.getItem() instanceof IModifiable){
            ToolStack tool = ToolStack.from(mainHandStack);
            var toolData = tool.getPersistentData();
            var modifierLevel = tool.getModifierLevel(CtiModifiers.REPLACED_HEAT_SWORD.getId());
            if (modifierLevel >0&&!player.hasEffect(CtiEffects.OVERHEAT.get())){
                toolData.putInt(ReplacedHeatSword.KEY_HEAT,Math.min(100, toolData.getInt(ReplacedHeatSword.KEY_HEAT)+ modifierLevel *5));
            }
            modifierLevel = tool.getModifierLevel(CtiModifiers.RUBY_LASER.getId());
            if (modifierLevel>0&&player.hasEffect(CtiEffects.OVERHEAT.get())){
                level+= (int) (player.getAttributeValue(Attributes.ATTACK_DAMAGE)*0.25f*modifierLevel);
            }
            if (Exothermic.getFuelTemp(tool,player)>0){
                level+= (int) (level*Exothermic.getFuelTemp(tool,null)/10000f);
            }
        }
        entity.invulnerableTime = 0;
        entity.hurt(ElementalDamageSource.fiery(player),level);
        entity.invulnerableTime = inv;
        if (player.getLevel() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(IPParticleTypes.FLARE_FIRE.get(), entity.getRandomX(1),entity.getY()+random.nextFloat()*entity.getBbHeight(),entity.getRandomZ(1),1,0,0,0,0);
    }
}
