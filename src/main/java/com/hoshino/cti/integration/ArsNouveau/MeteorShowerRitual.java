package com.hoshino.cti.integration.ArsNouveau;

import com.aetherteam.aether.capability.time.AetherTime;
import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hoshino.cti.Entity.Projectiles.AethericMeteor;
import com.hoshino.cti.Entity.Projectiles.MeteorEntity;
import com.hoshino.cti.Event.ModEvents.MeteorSpawnEvent;
import com.hoshino.cti.Cti;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.TimeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;
import java.util.Random;

import static com.hoshino.cti.util.CommonUtil.isAetherNight;

public class MeteorShowerRitual extends AbstractRitual {



    @Override
    protected void tick() {
        incrementProgress();
        if (getProgress() >= 1200) {
            setFinished();
        }

        if (getWorld() instanceof ServerLevel level&& level.dimension()== AetherDimensions.AETHER_LEVEL&& isAetherNight(level)&& getPos() != null && isRunning()) {
            for (int i =0;i<3;i++) {
                Random random = new Random();
                float x = random.nextFloat() * 48 - 24;
                float y = random.nextFloat() * 48 - 24;
                Vec2 pos = new Vec2(getPos().getX() + x, getPos().getZ() + y);
                MeteorSpawnEvent event1 = new MeteorSpawnEvent(new Vec3(pos.x, 350, pos.y));
                MinecraftForge.EVENT_BUS.post(event1);
                if (!event1.isCanceled()) {
                    AethericMeteor entity = new AethericMeteor(level, pos.x, 350, pos.y, new Vec3(random.nextFloat() * 0.5, random.nextFloat() * 2.5 - 1.5, random.nextFloat() * 0.5));
                    level.addFreshEntity(entity);
                }
            }
            this.getContext().progress+=4;
        } else if (getWorld() instanceof ServerLevel level && getPos() != null && isRunning()) {
            Random random = new Random();
            if (random.nextInt(20) == 0) {
                float x = random.nextFloat() * 128 - 64;
                float y = random.nextFloat() * 128 - 64;
                if (x == 0 && y == 0) {
                    x = 0.1f;
                }
                float v = (float) Math.sqrt(x * x + y * y);
                float x1 = x / v;
                float y1 = y / v;
                x += x < 0 ? -20 * x1 : 20 * x1;
                y += y < 0 ? -20 * y1 : 20 * y1;
                Vec2 pos = new Vec2(getPos().getX() + x, getPos().getZ() + y);
                MeteorSpawnEvent event1 = new MeteorSpawnEvent(new Vec3(pos.x, 350, pos.y));
                MinecraftForge.EVENT_BUS.post(event1);
                if (!event1.isCanceled()) {
                    MeteorEntity entity = new MeteorEntity(level, pos.x, 350, pos.y, new Vec3(random.nextFloat() * 0.5, random.nextFloat() * 2.5 - 1.5, random.nextFloat() * 0.5));
                    entity.setExplosionPower((byte) (random.nextInt(125) + 25));
                    level.addFreshEntity(entity);
                }
            }
            if (random.nextInt(5) == 0) {
                float x = random.nextFloat() * 128 - 64;
                float y = random.nextFloat() * 128 - 64;
                if (x == 0 && y == 0) {
                    x += 0.1f;
                }
                float v = (float) Math.sqrt(x * x + y * y);
                float x1 = x / v;
                float y1 = y / v;
                x += x < 0 ? -8 * x1 : 8 * x1;
                y += y < 0 ? -8 * y1 : 8 * y1;
                Vec2 pos = new Vec2(getPos().getX() + x, getPos().getZ() + y);
                MeteorSpawnEvent event1 = new MeteorSpawnEvent(new Vec3(pos.x, 350, pos.y));
                MinecraftForge.EVENT_BUS.post(event1);
                if (!event1.isCanceled()) {
                    MeteorEntity entity = new MeteorEntity(level, pos.x, 350, pos.y, new Vec3(random.nextFloat() * 0.5, random.nextFloat() * 2.5 - 1.5, random.nextFloat() * 0.5));
                    entity.setExplosionPower((byte) (random.nextInt(10) + 2));
                    level.addFreshEntity(entity);
                }
            }
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return Cti.getResource("meteor_shower");
    }

    @Override
    public ParticleColor getCenterColor() {
        return ParticleColor.makeRandomColor(255, 120, 10, rand);
    }

    @Override
    public String getLangDescription() {
        return "让雨点般密集的陨石从天上坠落，将你的世界焚烧殆尽。注：作用范围为84*84，这些陨石可能会摧毁你的火盆。";
    }
}
