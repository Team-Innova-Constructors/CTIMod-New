package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Entities.phantomswordentity;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.init.etshtinkerEntity;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.security.SecureRandom;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class ReplacedPhantomForged extends EtSTBaseModifier implements LeftClickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (!level.isClientSide&&player.getAttackStrengthScale(0)>0.8&&target instanceof LivingEntity living){
            phantomswordentity entity =new phantomswordentity(etshtinkerEntity.phantomswordentity.get(),player.level);
            entity.damage = (float) (0.25f*player.getAttributeValue(Attributes.ATTACK_DAMAGE));
            entity.count =entry.getLevel()+1;
            entity.target=living;
            entity.setOwner(player);
            entity.setPos(target.getX(),target.getY()+0.5*target.getBbHeight()+3.5,target.getZ());
            player.level.addFreshEntity(entity);
            int i = 0;
            while (i<5){
                SecureRandom random1 =EtSHrnd();
                ((ServerLevel)level).sendParticles(ParticleTypes.SCULK_SOUL, target.getX(), target.getY() + 0.5 * target.getBbHeight() + 3.5, target.getZ(), 1,0,0,0, random1.nextDouble() * 0.04 - 0.02);
                i++;
            }
            var center = player.getBoundingBox().getCenter();
            var distance = entry.getLevel()*2+player.getAttackRange();
            level.getEntitiesOfClass(LivingEntity.class,new AABB(center.add(-distance,-0.3*distance,-distance),center.add(distance,0.3*distance,distance)),living1 -> !(living1 instanceof Player)&&living1.isAlive()&&!living1.isAlliedTo(player)).forEach(living1 -> {
                phantomswordentity entity1 =new phantomswordentity(etshtinkerEntity.phantomswordentity.get(),player.level);
                entity1.damage = (float) (0.25f*player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                entity1.count =entry.getLevel()-1;
                entity1.target=living1;
                entity1.setOwner(player);
                entity1.setPos(living1.getX(),living1.getY()+0.5*living1.getBbHeight()+3.5,living1.getZ());
                entity1.level.addFreshEntity(entity1);
                int j = 0;
                while (j<5){
                    SecureRandom random1 =EtSHrnd();
                    ((ServerLevel)level).sendParticles(ParticleTypes.SCULK_SOUL, living1.getX(), living1.getY() + 0.5 * living1.getBbHeight() + 3.5, living1.getZ(), 1,0,0,0, random1.nextDouble() * 0.04 - 0.02);
                    j++;
                }
            });
        }
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (!level.isClientSide&&player.getAttackStrengthScale(0)>0.8){
            var center = player.getBoundingBox().getCenter();
            var distance = entry.getLevel()*2+player.getAttackRange();
            level.getEntitiesOfClass(LivingEntity.class,new AABB(center.add(-distance,-distance*0.3,-distance),center.add(distance,0.3*distance,distance)),living -> !(living instanceof Player)&&living.isAlive()&&!living.isAlliedTo(player)).forEach(living -> {
                phantomswordentity entity =new phantomswordentity(etshtinkerEntity.phantomswordentity.get(),player.level);
                entity.damage = (float) (0.25f*player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                entity.count =entry.getLevel()-1;
                entity.target=living;
                entity.setOwner(player);
                entity.setPos(living.getX(),living.getY()+0.5*living.getBbHeight()+3.5,living.getZ());
                player.level.addFreshEntity(entity);
                int i = 0;
                while (i<5){
                    SecureRandom random1 =EtSHrnd();
                    ((ServerLevel)level).sendParticles(ParticleTypes.SCULK_SOUL, living.getX(), living.getY() + 0.5 * living.getBbHeight() + 3.5, living.getZ(), 1,0,0,0, random1.nextDouble() * 0.04 - 0.02);
                    i++;
                }
            });
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (!level.isClientSide&&player.getAttackStrengthScale(0)>0.8){
            var center = player.getBoundingBox().getCenter();
            var distance = entry.getLevel()*2+player.getAttackRange();
            level.getEntitiesOfClass(LivingEntity.class,new AABB(center.add(-distance,-distance*0.3,-distance),center.add(distance,0.3*distance,distance)),living -> !(living instanceof Player)&&living.isAlive()&&!living.isAlliedTo(player)).forEach(living -> {
                phantomswordentity entity =new phantomswordentity(etshtinkerEntity.phantomswordentity.get(),player.level);
                entity.damage = (float) (0.25f*player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                entity.count =entry.getLevel()-1;
                entity.target=living;
                entity.setOwner(player);
                entity.setPos(living.getX(),living.getY()+0.5*living.getBbHeight()+3.5,living.getZ());
                player.level.addFreshEntity(entity);
                int i = 0;
                while (i<5){
                    SecureRandom random1 =EtSHrnd();
                    ((ServerLevel)level).sendParticles(ParticleTypes.SCULK_SOUL, living.getX(), living.getY() + 0.5 * living.getBbHeight() + 3.5, living.getZ(), 1,0,0,0, random1.nextDouble() * 0.04 - 0.02);
                    i++;
                }
            });
        }
    }
}
