package com.hoshino.cti.integration.ArsNouveau;

import cofh.thermal.core.entity.monster.Basalz;
import cofh.thermal.core.entity.monster.Blitz;
import cofh.thermal.core.entity.monster.Blizz;
import cofh.thermal.core.init.TCoreEntities;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hoshino.cti.Cti;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.phys.Vec3;

public class ElementalSummonRitual extends AbstractRitual {
    @Override
    protected void tick() {
        incrementProgress();
        var pos = getPos();
        var level = tile.getLevel();
        if (getProgress()>=40&&pos!=null&&level!=null){
            for (int i = 0; i < 4+rand.nextInt(5); i++) {
                var summonPos = Vec3.atBottomCenterOf(pos).add(rand.nextDouble()*6-3,rand.nextDouble()*3+1,rand.nextDouble()*6-3);
                var mob = switch (rand.nextInt(4)){
                    case 1 -> new Basalz(TCoreEntities.BASALZ.get(),level);
                    case 2 -> new Blitz(TCoreEntities.BLITZ.get(), level);
                    case 3 -> new Blizz(TCoreEntities.BLIZZ.get(), level);
                    default -> new Blaze(EntityType.BLAZE,level);
                };
                mob.setPos(summonPos);
                level.addFreshEntity(mob);
            }
            setFinished();
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return Cti.getResource("elemental_summon");
    }

    @Override
    public ParticleColor getCenterColor() {
        return new ParticleColor(this.rand.nextInt(255), this.rand.nextInt(255), this.rand.nextInt(255));
    }

    @Override
    public String getLangDescription() {
        return "随机召唤4~8只烈焰人/狂风人/暴雪人/岩石人。它们会生成在以祭坛为中心正上方的5*5*5范围内。";
    }
}
