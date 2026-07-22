package com.hoshino.cti.Modifier.Contributors;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.util.Optional;

public class BloodGodBless extends ArmorModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }
}
