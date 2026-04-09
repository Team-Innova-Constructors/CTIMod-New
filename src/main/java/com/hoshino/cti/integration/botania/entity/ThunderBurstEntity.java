package com.hoshino.cti.integration.botania.entity;

import com.hoshino.cti.api.interfaces.IToolProvider;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.register.CtiEntity;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
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
        super.particles();
    }

    @Override
    public int getColor() {
        return switch (random.nextInt(5)){
            case 1-> 0xBFFFED;
            case 2-> 0x9CFFEA;
            case 3-> 0x61FFED;
            case 4-> 0x34FAFF;
            default -> 0x7DACFF;
        };
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
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
