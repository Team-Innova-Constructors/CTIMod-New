package com.hoshino.cti.mixin.BotaniaMixin;

import com.hoshino.cti.api.interfaces.IToolProvider;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.entity.ManaBurstEntity;

@Mixin(value = ManaBurstEntity.class,remap = false)
public abstract class ManaBurstEntityMixin extends ThrowableProjectile implements IManaBurstExtra, IToolProvider {
    @Unique
    private IntOpenHashSet cti$hitEntityIds = new IntOpenHashSet();
    @Unique
    private float cti$baseDamage;
    @Unique
    private int cti$perConsumption=50;
    @Unique
    private int cti$perBlockConsumption=50;
    @Unique
    private IToolStackView cti$tool;
    @Unique int cti$generation = 0;
    @Unique float cti$damageModifier = 1;

    protected ManaBurstEntityMixin(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    @Unique
    public int cti$getGeneration() {
        return cti$generation;
    }
    @Override
    @Unique
    public void cti$setGeneration(int i){
        this.cti$generation = i;
    }

    @Unique
    @Override
    public void cti$clearHitList() {
        this.cti$hitEntityIds.clear();
    }


    @Unique
    @Override
    public float cti$getDamageModifier(){
        return cti$damageModifier;
    }
    @Unique
    @Override
    public void cti$setDamageModifier(float f){
        cti$damageModifier = f;
    }

    @Override
    @Unique
    protected boolean canHitEntity(Entity pTarget) {
        if (pTarget==getOwner()) return false;
        if (cti$hitEntityIds.contains(pTarget.getId())) return false;
        if (pTarget instanceof ItemEntity ||pTarget instanceof ExperienceOrb) return false;
        if (pTarget instanceof Projectile) return false;
        return !(pTarget instanceof Player);
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
}
