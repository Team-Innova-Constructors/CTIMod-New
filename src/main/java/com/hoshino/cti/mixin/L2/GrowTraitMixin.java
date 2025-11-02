package com.hoshino.cti.mixin.L2;

import com.aetherteam.aether.entity.monster.Swet;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.GrowthTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GrowthTrait.class,remap = false)
public abstract class GrowTraitMixin extends MobTrait {
    public GrowTraitMixin(ChatFormatting format) {
        super(format);
    }
    /**
     * @author firefly
     * @reason 不让史维特带这个词条
     */
    @Overwrite
    public boolean allow(@NotNull LivingEntity le, int difficulty, int maxModLv) {
        return le instanceof Slime && super.allow(le, difficulty, maxModLv)&&!(le instanceof Swet);
    }
    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;m_7839_(IZ)V"), cancellable = true)
    private void setSize(LivingEntity mob, int level, CallbackInfo ci){
        if(mob instanceof Slime slime){
            var size=slime.getSize();
            slime.setSize(Math.max(size,level+3),true);
            ci.cancel();
        }
    }
}
