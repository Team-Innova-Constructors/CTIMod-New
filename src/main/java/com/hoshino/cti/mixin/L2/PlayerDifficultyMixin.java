package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.register.CtiItem;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = PlayerDifficulty.class, remap = false)
public abstract class PlayerDifficultyMixin extends PlayerCapabilityTemplate<PlayerDifficulty> {
    @Inject(method = "apply", at = @At("TAIL"))
    private void onApply(MobDifficultyCollector instance, CallbackInfo ci) {
        Player player = this.player;
        if (CurioCompat.hasItem(player, CtiItem.gossip_of_ratatoskr.get())) {
            PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(player);
            LevelEditor editor = cap.getLevelEditor();
            int total = editor.getTotal();
            double b = Mth.clamp(1 - (double) total / 8000, 0, 1);
            instance.traitCostFactor(b);
            instance.setFullChance();
        }
    }
}