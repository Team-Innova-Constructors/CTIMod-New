package com.hoshino.cti.Blocks;

import com.c2h6s.etshtinker.Entities.plasmaexplosionentity;
import com.c2h6s.etshtinker.init.etshtinkerEntity;
import com.c2h6s.etshtinker.init.etshtinkerParticleType;
import com.hoshino.cti.register.CtiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class OverdenseGlacioStone extends Block {

    public OverdenseGlacioStone(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int rnd = pRandom.nextInt(300);
        if (rnd <= 20) {
            BlockPos blockpos = new BlockPos(pPos.getX(), pPos.getY() + 1, pPos.getZ());
            BlockState blockstate = pLevel.getBlockState(blockpos);
            int a = 0;
            while (a <= 20 && !(blockstate.is(Blocks.AIR) || blockstate.is(Blocks.CAVE_AIR) || blockstate.is(Blocks.VOID_AIR) || blockstate.is(Blocks.WATER))) {
                blockpos = blockpos.above();
                blockstate = pLevel.getBlockState(blockpos);
                a++;
            }
            if (a < 20) {
                if (pLevel.getBlockState(blockpos.below()).is(CtiBlock.unipolar_magnet_budding.get()) && rnd != 0) {
                    Block block = CtiBlock.unipolar_magnet.get();
                    BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.UP).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                    pLevel.setBlockAndUpdate(blockpos, blockState);
                    plasmaexplosionentity entity = new plasmaexplosionentity(etshtinkerEntity.plasmaexplosionentity.get(), pLevel);
                    entity.particle = etshtinkerParticleType.plasmaexplosionpurple.get();
                    entity.special = "entropic";
                    entity.scale = 1.5f;
                    entity.damage = 200;
                    entity.rayVec3 = new Vec3(0, pRandom.nextInt(12) + 4, 0);
                    entity.setPos(blockpos.getX(), blockpos.getY() - 2, blockpos.getZ());
                    pLevel.addFreshEntity(entity);
                } else if (rnd == 0) {
                    plasmaexplosionentity entity = new plasmaexplosionentity(etshtinkerEntity.plasmaexplosionentity.get(), pLevel);
                    entity.particle = etshtinkerParticleType.plasmaexplosionpurple.get();
                    entity.special = "entropic";
                    entity.scale = 0.75f;
                    entity.damage = 30;
                    entity.rayVec3 = new Vec3(0, pRandom.nextInt(6) + 2, 0);
                    entity.setPos(blockpos.getX(), blockpos.getY() - 2, blockpos.getZ());
                    pLevel.addFreshEntity(entity);
                }
            }
        }
    }
}
