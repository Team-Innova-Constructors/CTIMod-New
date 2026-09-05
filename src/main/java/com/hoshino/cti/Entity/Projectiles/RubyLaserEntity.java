package com.hoshino.cti.Entity.Projectiles;

import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.entityTicker.tickers.Fiery;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.util.AttackUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.hoshino.cti.util.EntityUtil.inflictBurnt;

public class RubyLaserEntity extends Projectile {
    public RubyLaserEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public static final EntityDataAccessor<Float> DATA_LENGTH = SynchedEntityData.defineId(RubyLaserEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DATA_RENDER = SynchedEntityData.defineId(RubyLaserEntity.class, EntityDataSerializers.BOOLEAN);
    public ToolStack tool;
    public boolean OffHand = false;
    public int burnt = 10;
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LENGTH,0f);
        this.entityData.define(DATA_RENDER,false);
    }
    public IntOpenHashSet piercedEntityList = new IntOpenHashSet();

    @Override
    public boolean isAttackable() {
        return false;
    }

    public boolean readyToRender(){
        return this.entityData.get(DATA_RENDER);
    }

    public void setDataLength(float amount){
        this.entityData.set(DATA_LENGTH,amount);
    }
    public float getDataLength(){
        return this.entityData.get(DATA_LENGTH);
    }


    @Override
    protected boolean canHitEntity(Entity pTarget) {
        if (pTarget instanceof Projectile||
                pTarget instanceof ItemEntity ||
                pTarget instanceof ExperienceOrb ||
                pTarget==this.getOwner()) return false;
        if (piercedEntityList.contains(pTarget.getId())) return false;
        if (pTarget instanceof LivingEntity){
            if (this.getOwner() instanceof Player player1){
                if (pTarget instanceof Player player) return player1.canHarmPlayer(player);
            }
        }
        return pTarget.isAlive();
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (this.firstTick&&this.getOwner() instanceof Player player) {
                this.tickCount = 0;
                Vec3 initialPos = new Vec3(this.getX(),this.getY(),this.getZ());
                double distance =this.getDataLength();
                float scale = 1;
                Vec3 direction = this.getDeltaMovement().normalize();
                Vec3 step = direction.scale(scale*0.5);
                this.setDeltaMovement(step);
                Vec3 end = null;
                HitResult hitResult = this.level.clip(new ClipContext(initialPos,initialPos.add(direction.scale(distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,null));
                for (double i = 0; i <= distance; i += scale*0.5) {
                    Vec3 pos = initialPos.add(direction.scale(i));
                    AABB aabb = new AABB(pos.x-scale*0.5,pos.y-scale*0.5,pos.z-scale*0.5,pos.x+scale*0.5,pos.y+scale*0.5,pos.z+scale*0.5);
                    aabb.expandTowards(step);
                    List<Entity> entities = this.level.getEntitiesOfClass(Entity.class,aabb,this::canHitEntity);
                    for (Entity entity:entities){
                        if (entity instanceof LivingEntity living){
                            if (player.hasEffect(CtiEffects.OVERHEAT.get()))
                                AttackUtil.attackEntity(tool,player, InteractionHand.MAIN_HAND,entity,()->1,false, EquipmentSlot.MAINHAND,true, (float) (player.getAttributeValue(Attributes.ATTACK_DAMAGE)*2f),0.75f);
                            inflictBurnt(player,living,tool,burnt);
                            var burnt = EntityTickerManager.getInstancePlayerSpecific(living,player.getUUID()).getTicker(CtiEntityTickers.FIERY.get());
                            if (burnt!=null){
                                Fiery.burntDamage(living,player,burnt.level);
                            }
                            if (hitResult.getType() != HitResult.Type.ENTITY) hitResult = new EntityHitResult(living,pos.add(step.scale(0.5)));
                            piercedEntityList.add(entity.getId());
                        }
                    }
                    end = pos.add(step.scale(0.5));
                }
                if (hitResult.getType()== HitResult.Type.MISS){
                    Vec3 path = direction.scale(distance);
                    Vec3 offset = player.getLookAngle().cross(new Vec3(0,1,0)).normalize().scale(0.6f);
                    if (random.nextBoolean()) offset = offset.reverse();
                    Vec3 randomOffset = offset.cross(player.getLookAngle()).normalize().scale(random.nextFloat()*0.5f-0.25f);
                    offset = offset.add(randomOffset);
                    Vec3 newDirection = path.subtract(offset).normalize();
                    this.setDeltaMovement(newDirection);
                    this.setPos(initialPos.add(offset));
                    this.xOld=this.getX();
                    this.yOld=this.getY();
                    this.zOld=this.getZ();
                    this.entityData.set(DATA_RENDER,true);
                } else {
                    Vec3 path;
                    if (end!=null) path = end.subtract(initialPos);
                    else path = hitResult.getLocation().subtract(initialPos);
                    Vec3 offset = player.getLookAngle().cross(new Vec3(0.01,1,0.01)).normalize().scale(0.6f);
                    if (random.nextBoolean()) offset = offset.reverse();
                    Vec3 randomOffset = offset.cross(player.getLookAngle()).normalize().scale(random.nextFloat()*0.5f-0.25f);
                    offset = offset.add(randomOffset);
                    Vec3 newDirection = path.subtract(offset).normalize();
                    this.setDeltaMovement(newDirection);
                    this.setPos(initialPos.add(offset));
                    this.xOld=this.getX();
                    this.yOld=this.getY();
                    this.zOld=this.getZ();
                    this.entityData.set(DATA_RENDER,true);
                }
            }
            if (this.tickCount >= 9) {
                this.discard();
            }
        }
        super.tick();
    }
}
