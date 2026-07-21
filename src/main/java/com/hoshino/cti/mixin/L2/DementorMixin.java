package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.ILivingEntityMixin;
import com.hoshino.cti.util.L2.ExHurtHelper;
import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.traits.legendary.DementorTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import mekanism.common.registries.MekanismItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import slimeknights.tconstruct.library.modifiers.ModifierId;

@Mixin(value = DementorTrait.class, remap = false)
public class DementorMixin extends LegendaryTrait {
    public DementorMixin(ChatFormatting format) {
        super(format);
    }

    /**
     * @author FireFly
     * @reason 摄魂判定问题，此形参无法正确检测isBypassArmor属性,因此mixin掉,不再免疫
     */
    @Overwrite
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
    }

    /**
     * @author firefly
     * @reason 不让反射伤害被修改
     */
    @Overwrite
    public void onCreateSource(int level, LivingEntity attacker, LivingAttackEvent event) {
        if (event.getSource().getMsgId().equals("mobattackreflect")) return;
        if (event.getSource().getMsgId().equals("dispell")) return;
        if (event.getSource().getMsgId().equals("dementor")) return;
        event.getSource().bypassArmor();
    }


    @Override
    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
        if (ExHurtHelper.shouldHurt(target)) {
            var source = new EntityDamageSource("dementor", attacker);
            var mobLevel = DifficultyLevel.ofAny(attacker);
            float scale = ExHurtHelper.getScale(target);
            float totalHurt=mobLevel * 0.03f * level *scale;
            if(totalHurt==0)return;
            if(target.isDeadOrDying()||!target.isAlive())return;
            target.invulnerableTime=0;
            ((ILivingEntityMixin)target).cti$strictHurt(source,totalHurt,false);
        }
    }
}
