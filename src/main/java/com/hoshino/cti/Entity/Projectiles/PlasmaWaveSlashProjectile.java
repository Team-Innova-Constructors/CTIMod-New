package com.hoshino.cti.Entity.Projectiles;

import cofh.core.init.CoreMobEffects;
import com.c2h6s.etshtinker.Entities.ItemProjectile;
import com.c2h6s.etshtinker.init.ItemReg.etshtinkerItems;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.hoshino.cti.util.AttackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;

public class PlasmaWaveSlashProjectile extends ItemProjectile {
    public int time =0;
    public IToolStackView tool =null;
    public List<Entity> hitList = new ArrayList<>();
    public PlasmaWaveSlashProjectile(EntityType<? extends ItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }

    @Override
    protected void defineSynchedData() {
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        time++;
        Vec3 movement =this.getDeltaMovement();
        if (time>=5){
            this.remove(RemovalReason.DISCARDED);
        }
        this.setPos(movement.x+this.getX(),movement.y+this.getY(),movement.z+this.getZ());
        super.tick();
        List<LivingEntity> ls =this.level.getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().expandTowards(movement),living -> !hitList.contains(living));
        for (LivingEntity entity :ls){
            if (entity!=null&&entity!=this.getOwner()&&this.getOwner() instanceof Player player&&!(entity instanceof Player)){
                if (tool!=null) {
                    entity.invulnerableTime = 0;
                    AttackUtil.attackEntity(tool,player, InteractionHand.MAIN_HAND,entity,()->1,true, EquipmentSlot.MAINHAND,0.25f);
                    entity.invulnerableTime =0;
                    hitList.add(entity);
                }

                entity.forceAddEffect(new MobEffectInstance(CoreMobEffects.SHOCKED.get(),1000,3,false,false),player);
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(etshtinkerItems.plasmawaveslash.get());
    }
}
