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
        if (event.getEntity() instanceof ServerPlayer player && event.getSource().getEntity() != null) {
            if (event.getAmount() > EnigmaticItems.THE_CUBE.getDamageLimit(player) && SuperpositionHandler.hasCurio(player, EnigmaticItems.THE_CUBE)) {
                player.level.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1F, 1F);
                if (event.getSource().getDirectEntity() instanceof LivingEntity living) {
                    event.setAmount(event.getAmount() * 0.3f);
                    Vector3 look = new Vector3(living.position()).subtract(new Vector3(player.position())).normalize();
                    Vector3 dir = look.multiply(1D);
                    etheriumConfig.knockBack(living, 1.0F, dir.x, dir.z);
                }
            }
        }
    }
}
