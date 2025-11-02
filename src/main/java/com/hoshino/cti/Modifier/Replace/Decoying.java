package com.hoshino.cti.Modifier.Replace;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class Decoying extends BattleModifier {

    public void SummonLivingEntity(Level Level, Vec3 Position, LivingEntity lv) {
        Level.addFreshEntity(lv);
        lv.moveTo(Position);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if ((context.getAttacker() instanceof Player)) {
            LivingEntity target = context.getLivingTarget();
            int random = RANDOM.nextInt(Math.min(modifier.getLevel(), 5));
            if (target != null) {
                Level level = target.getLevel();
                List<Mob> spawns =level.getEntitiesOfClass(Mob.class,new AABB(target.getOnPos()).inflate(10));
                if(spawns.size()<20){
                    Chicken chicken = EntityType.CHICKEN.create(level);
                    Pig pig = EntityType.PIG.create(level);
                    Cow cow = EntityType.COW.create(level);
                    Sheep sheep = EntityType.SHEEP.create(level);
                    Rabbit rabbit = EntityType.RABBIT.create(level);
                    LivingEntity[] livingEntities = new LivingEntity[]{
                            chicken, pig, cow, sheep, rabbit
                    };
                    this.SummonLivingEntity(level, target.getPosition(0), livingEntities[random]);
                }
            }
        }
    }
}
