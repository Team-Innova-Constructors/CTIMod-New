package com.hoshino.cti.Blocks.BlockEntity.botania;

import com.c2h6s.etshtinker.init.etshtinkerParticleType;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PParticleRingS2C;
import com.hoshino.cti.recipe.RecipeMap;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.util.ParticleContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.EntityHelper;

import java.util.ArrayList;
import java.util.List;

import static com.hoshino.cti.recipe.RecipeMap.getReactiveFlowerRecipes;

public class ReactiveFLowerBE extends GeneratingFlowerBlockEntity {
    private static final String TAG_BURN_TIME = "burnTime";
    private static final String TAG_PER_GENERATION = "perGeneration";
    private static final int RANGE = 3;

    private int burnTime = 0;
    private int perGeneration = 0;
    private int blaze = 0;
    private int ice = 0;
    private int air = 0;
    private int earth = 0;
    private int tickCollecting = 0;
    private List<ElementalType> usedTypes = new ArrayList<>();

    public ReactiveFLowerBE(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.REACTIVE_FLOWER.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();


        if (getLevel().isClientSide) {
            if (burnTime > 0 && getLevel().random.nextInt(10) == 0) {
                emitParticle(etshtinkerParticleType.nova.get(), 0.2 + Math.random() * 0.6, 0.7, 0.2 + Math.random() * 0.6, 0.0D, 0.0D, 0.0D);
            }
            return;
        } else {
            if (burnTime > 0&&perGeneration>0) {
                var needed = getMaxMana()-getMana();
                needed = Math.min(needed,perGeneration);
                addMana(needed);
                burnTime-=needed;
            }
        }

        if (burnTime == 0) {
            if (tickCollecting<=40&&usedTypes.size()<4) {
                if (getMana() < getMaxMana()) {
                    for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                        ItemStack stack = item.getItem();
                        var type = getElementalType(stack);
                        if (type != null) {
                            if (usedTypes.contains(type)) continue;
                            int i = getElementalValue(stack);
                            switch (type) {
                                case ICE -> ice += i;
                                case BLAZE -> blaze += i;
                                case EARTH -> earth += i;
                                case AERIAL -> air += i;
                            }
                            usedTypes.add(type);
                            EntityHelper.shrinkItem(item);
                        }
                    }
                    tickCollecting++;
                }
            } else {
                tickCollecting = 0;
                var manaTotal = (Math.min(blaze, ice) * 2) + (Math.min(air, earth) * 2);
                var manaPerTick = Mth.clamp(manaTotal / 1000, 1, 2000);
                if (manaTotal>0) {
                    burnTime = manaTotal;
                    perGeneration = manaPerTick;
                    blaze = 0;
                    ice = 0;
                    air = 0;
                    earth = 0;
                    usedTypes.clear();
                    level.playSound(null, getEffectivePos(), BotaniaSounds.endoflame, SoundSource.BLOCKS, 1F, 1F);
                    if (level instanceof ServerLevel) {
                        CtiPacketHandler.sendToClient(new PParticleRingS2C(0.5f, 0.05f, Vec3.atCenterOf(getBlockPos()).add(0,0.2,0), etshtinkerParticleType.quark_disassemble.get(), 30));
                    }
                    sync();
                }
            }

        }
    }


    @Override
    public int getMaxMana() {
        return 2000;
    }

    @Override
    public int getColor() {
        return 0x7C56FF;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);
        cmp.putInt(TAG_BURN_TIME, burnTime);
        cmp.putInt(TAG_PER_GENERATION, perGeneration);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInt(TAG_BURN_TIME);
        perGeneration = cmp.getInt(TAG_PER_GENERATION);
    }

    public static int getElementalValue(ItemStack stack) {
        var item = stack.getItem();
        if (getReactiveFlowerRecipes().containsKey(item))
            return getReactiveFlowerRecipes().get(item).elementalValue;
        return 0;
    }

    @Nullable
    public static ElementalType getElementalType(ItemStack stack){
        var item = stack.getItem();
        if (getReactiveFlowerRecipes().containsKey(item))
            return getReactiveFlowerRecipes().get(item).elementalType;
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap== BotaniaForgeClientCapabilities.WAND_HUD)
            return LazyOptional.of(()->new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(this)).cast();
        return super.getCapability(cap, side);
    }


    public enum ElementalType{
        EARTH,
        BLAZE,
        AERIAL,
        ICE
    }
}
