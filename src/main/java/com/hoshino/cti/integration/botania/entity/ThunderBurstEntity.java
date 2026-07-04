package com.hoshino.cti.integration.botania.entity;

import cofh.core.client.particle.options.BiColorParticleOptions;
import cofh.core.init.CoreParticles;
import com.hoshino.cti.api.interfaces.IToolProvider;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.register.CtiEntity;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.entity.ManaBurstEntity;


public class ThunderBurstEntity extends ManaBurstEntity implements IManaBurstExtra, IToolProvider {
    private IntOpenHashSet cti$hitEntityIds = new IntOpenHashSet();
    private float cti$baseDamage;
    private int cti$perConsumption=50;
    private int cti$perBlockConsumption=50;
    private IToolStackView cti$tool;
    int cti$generation = 0;
    float cti$damageModifier = 1;
    @Override
    public int cti$getGeneration() {
        return cti$generation;
    }
    @Override
    public void cti$setGeneration(int i){
        this.cti$generation = i;
    }
    @Override
    public void cti$clearHitList() {
        this.cti$hitEntityIds.clear();
    }
    @Override
    public float cti$getDamageModifier(){
        return cti$damageModifier;
    }
    @Override
    public void cti$setDamageModifier(float f){
        cti$damageModifier = f;
    }
    @Override
    public IntOpenHashSet cti$getHitEntityIdList() {
        return cti$hitEntityIds;
    }
    @Override
    public void cti$addToHitList(Entity entity) {
        cti$hitEntityIds.add(entity.getId());
    }
    @Override
    public float cti$getBaseDamage() {
        return cti$baseDamage;
    }
    @Override
    public void cti$setBaseDamage(float baseDamage) {
        this.cti$baseDamage=baseDamage;
    }
    @Override
    public int cti$getPerConsumption() {
        return cti$perConsumption;
    }
    @Override
    public void cti$setPerConsumption(int i) {
        cti$perConsumption=i;
    }
    @Override
    public int cti$getPerBlockConsumption() {
        return cti$perBlockConsumption;
    }
    @Override
    public void cti$setPerBlockConsumption(int i) {
        cti$perBlockConsumption = i;
    }
    @Override
    public IToolStackView cti$getTool() {
        return cti$tool;
    }
    @Override
    public void cti$setTool(IToolStackView tool) {
        this.cti$tool = tool;
    }

    public ThunderBurstEntity(EntityType<ManaBurstEntity> type, Level world) {
        super(type, world);
    }
    public ThunderBurstEntity(Level level){
        this(CtiEntity.THUNDER_BURST.get(),level);
    }
    public ThunderBurstEntity(Player player){
        this(player.level);
        this.setOwner(player);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult hit) {
    }

    @Override
    public void particles() {
    }

    public int getRGBA(){
        return switch (random.nextInt(5)){
            case 1-> 0xBFFFEDFF;
            case 2-> 0x9CFFEAFF;
            case 3-> 0x61FFEDFF;
            case 4-> 0x34FAFFFF;
            default -> 0x7DACFFFF;
        };
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
    }

    @Override
    public void tick() {
        if (this.firstTick&&this.level instanceof ServerLevel serverLevel) {
            var start = new Vec3(this.getX() + random.nextFloat() * 0.05f - 0.1, this.getY() , this.getZ() + random.nextFloat() * 0.05f - 0.1);
            var end = this.position().add(this.getDeltaMovement().scale(6)).add(random.nextFloat() * 0.05f - 0.1,0,random.nextFloat() * 0.05f - 0.1);
            serverLevel.sendParticles(new BiColorParticleOptions(CoreParticles.STRAIGHT_ARC.get(), 0.3F, 6.0F, 0.0F, -1, getRGBA()),
                    start.x,start.y,start.z,0,end.x,end.y,end.z,1);
        }
        super.tick();
        this.level.getEntitiesOfClass(Entity.class,this.getBoundingBox().expandTowards(this.getDeltaMovement()),this::canHitEntity).forEach(entity ->
                this.onHitEntity(new EntityHitResult(entity)));
        if (this.tickCount>6) this.discard();
    }

    @Override
    public boolean canHitEntity(Entity pTarget) {
        if (pTarget==getOwner()) return false;
        if (cti$hitEntityIds.contains(pTarget.getId())) return false;
        if (pTarget instanceof ItemEntity ||pTarget instanceof ExperienceOrb) return false;
        if (pTarget instanceof Projectile) return false;
        return !(pTarget instanceof Player);
    }


}
