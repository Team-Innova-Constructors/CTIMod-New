package com.hoshino.cti.util;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import slimeknights.mantle.block.entity.MantleBlockEntity;
import slimeknights.tconstruct.common.TinkerTags.EntityTypes;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipeCache;
import slimeknights.tconstruct.smeltery.block.entity.module.EntityMeltingModule;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * 自定义实体熔炼模块：在 Tinkers 原版 {@link EntityMeltingModule} 的行为基础上，
 * 将熔炼产物的数量乘以一个倍率（{@link #multiplierSupplier}），
 * 即"熔炼实体时原有产物产出更多"，不引入新的配方类型。
 *
 * <p>由 {@code soul_forge} 多方块冶炼炉在 {@code heat()} 中调用。</p>
 */
public class CtiEntityMeltingModule {
    public static final DamageSource SMELTERY_DAMAGE = EntityMeltingModule.SMELTERY_DAMAGE;
    public static final DamageSource SMELTERY_MAGIC = EntityMeltingModule.SMELTERY_MAGIC;

    private final MantleBlockEntity parent;
    private final IFluidHandler tank;
    private final BooleanSupplier canMeltEntities;
    private final Function<ItemStack, ItemStack> insertFunction;
    private final Supplier<AABB> bounds;
    /** 产物数量倍率供应商，允许根据多方块大小/状态动态调整 */
    private final IntSupplier multiplierSupplier;

    public CtiEntityMeltingModule(MantleBlockEntity parent, IFluidHandler tank,
                                  BooleanSupplier canMeltEntities,
                                  Function<ItemStack, ItemStack> insertFunction,
                                  Supplier<AABB> bounds,
                                  IntSupplier multiplierSupplier) {
        this.parent = parent;
        this.tank = tank;
        this.canMeltEntities = canMeltEntities;
        this.insertFunction = insertFunction;
        this.bounds = bounds;
        this.multiplierSupplier = multiplierSupplier;
    }

    private Level getLevel() {
        return Objects.requireNonNull(parent.getLevel(), "Parent tile entity has null world");
    }

    private boolean canMeltEntity(LivingEntity entity) {
        return true;
    }

    /**
     * 与结构内的实体交互，返回本次实际熔炼（造成伤害并产出流体）的实体数量。
     * 调用方可据此触发燃料消耗、产出压缩空气/热量等副作用。
     * 产物数量会按 {@link #multiplierSupplier} 给出的倍率放大。
     */
    public int interactWithEntities(int temp) {
        AABB boundingBox = bounds.get();
        if (boundingBox == null) {
            return 0;
        }

        int multiplier = Math.max(1, multiplierSupplier.getAsInt());
        Boolean canMelt = null;
        int meltedCount = 0;
        for (Entity entity : getLevel().getEntitiesOfClass(Entity.class, boundingBox)) {
            if (!entity.isAlive()) {
                continue;
            }

            EntityType<?> type = entity.getType();
            if (canMelt != Boolean.FALSE && !type.is(EntityTypes.MELTING_HIDE) && entity instanceof LivingEntity living && canMeltEntity(living)) {
                if (canMelt == null) {
                    canMelt = canMeltEntities.getAsBoolean();
                }
                if (canMelt) {
                    if (temp>=7000&&living.getHealth()<5000)
                        EntityTickerManager.getInstance(living).addTickerSimple(new EntityTickerInstance(CtiEntityTickers.EMP.get(), 1,20));
                    // 直接复用 Tinkers 的 EntityMeltingRecipe
                    EntityMeltingRecipe recipe = EntityMeltingRecipeCache.findRecipe(getLevel().getRecipeManager(), type);
                    FluidStack fluid;
                    int damage;
                    if (recipe != null) {
                        fluid = recipe.getOutput(living);
                        damage = recipe.getDamage();
                    } else {
                        fluid = EntityMeltingModule.getDefaultFluid();
                        damage = 2;
                    }
                    // 放大产物数量：原有产物产出更多
                    if (multiplier > 1 && !fluid.isEmpty()) {
                        fluid = new FluidStack(fluid.getFluid(), fluid.getAmount() * multiplier, fluid.getTag());
                    }
                    entity.invulnerableTime = 0;
                    var source = new DamageSource("cti_soulforge").bypassArmor().bypassEnchantments()
                            .bypassInvul().bypassMagic();
                    if (temp>=7000||entity.hurt(source, damage)) {
                        tank.fill(fluid, FluidAction.EXECUTE);
                        meltedCount++;
                    }
                }
            }
        }
        return meltedCount;
    }
}
