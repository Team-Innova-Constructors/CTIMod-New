package com.hoshino.cti.mixin.Eni;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.aizistral.enigmaticlegacy.EnigmaticLegacy.etheriumConfig;

@Mixin(value = EnigmaticEventHandler.class, remap = false)
public abstract class CubeMixin {
    /**
     * @author firefly
     * @reason 现在获取成本过低,
     * <br>削弱非欧立方限伤效果 无效化->原伤害30%
     */
    @SubscribeEvent(priority = EventPriority.LOW)
    @Overwrite
    public void endEntityHurt(LivingHurtEvent event) {
    }
}
