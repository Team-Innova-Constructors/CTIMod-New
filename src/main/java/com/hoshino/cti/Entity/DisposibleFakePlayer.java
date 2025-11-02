package com.hoshino.cti.Entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;

public class DisposibleFakePlayer extends FakePlayer {
    public DisposibleFakePlayer(ServerLevel level, GameProfile name) {
        super(level, name);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= 50) {
            this.discard();
        }
    }
}
