package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.ILivingEntityMixin;
import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.traits.legendary.DementorTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.EntityDamageSource;
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
        if(event.getSource().getMsgId().equals("mobattackreflect"))return;
        if(event.getSource().getMsgId().equals("dispell"))return;
        if(event.getSource().getMsgId().equals("dementor"))return;
        event.getSource().bypassArmor();
    }


    @Override
    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
        if(cti_new$shouldHurt(target)){
            var source=new EntityDamageSource("dementor",attacker);
            var mobLevel=DifficultyLevel.ofAny(attacker);
            float scale=cti_new$getScale(target);
            float totalHurt=mobLevel * 0.02f * level *scale;
            ((ILivingEntityMixin)target).cti$strictHurt(source,totalHurt);
        }
    }
    @Unique
    private boolean cti_new$shouldHurt(LivingEntity living){
        if(!(living instanceof Player player))return true;
        if(GetModifierLevel.CurioHasModifierlevel(player,new ModifierId("solidarytinker:bha")))return false;
        if(GetModifierLevel.EquipHasModifierlevel(player,new ModifierId("cti:infinity")))return false;
        if(GetModifierLevel.EquipHasModifierlevel(player,new ModifierId("cti:trauma")))return false;
        return !GetModifierLevel.EquipHasModifierlevel(player, new ModifierId("cti:all"));
    }
    @Unique
    private float cti_new$getScale(LivingEntity living){
        if(!(living instanceof Player player))return 1;
        var relicLevel=GetModifierLevel.getAllSlotModifierlevel(player,new ModifierId("cti:the_relic"));
        var shadowOfVigridLevel=GetModifierLevel.getAllSlotModifierlevel(player,new ModifierId("cti:shadow_of_vigrid"));
        boolean hashardLevel=GetModifierLevel.getAllSlotModifierlevel(player,new ModifierId("etshtinker:solidex"))>0;
        float hasHard=1;
        if(hashardLevel){
            hasHard=0.8f;
        }
        return (1-(relicLevel * 0.03f)) * (1-(shadowOfVigridLevel * 0.08f)) *hasHard;
    }
}
