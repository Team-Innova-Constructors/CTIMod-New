package com.hoshino.cti.mixin.MowzieMixin;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityUmvuthi.SpawnFollowersAbility.class)
public class EntityUmvuthiAbilityMixin extends Ability<EntityUmvuthi> {
    public EntityUmvuthiAbilityMixin(AbilityType<EntityUmvuthi, ? extends Ability> abilityType, EntityUmvuthi user, AbilitySection[] sectionTrack, int cooldownMax) {
        super(abilityType, user, sectionTrack, cooldownMax);
    }

    @Inject(method = "beginSection", at = @At(value = "HEAD"), cancellable = true,remap = false)
    private void preventBeginSection(AbilitySection section, CallbackInfo ci) {
        var user = this.getUser();
        if (user.getLevel().isClientSide()) return;
        if (section.sectionType != AbilitySection.AbilitySectionType.ACTIVE) {
            return;
        }
        var data = user.getPersistentData();
        int time = data.getInt("umvuthi_summon");
        float per = user.getHealth() / user.getMaxHealth();
        if (!((per > 0.6 && per < 0.9 && time > 2) || (per > 0.3 && per <= 0.6 && time > 1) || (per <= 0.3 && time > 0))) {
            ci.cancel();
        }
    }
    @Inject(method = "beginSection",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",shift = At.Shift.AFTER))
    private void onAdd(AbilitySection section, CallbackInfo ci){
        var user = this.getUser();
        if (user.getLevel().isClientSide()) return;
        var data = user.getPersistentData();
        int time = data.getInt("umvuthi_summon");
        data.putInt("umvuthi_summon",time-1);
    }
}
