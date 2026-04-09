package com.hoshino.cti.integration.botania.modifiers.specialized;

import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.hook.UpdateBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.modifiers.base.SpecializedBurstModifier;
import com.hoshino.cti.util.EntityInRangeUtil;
import com.hoshino.cti.util.ProjectileUtil;
import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.entity.ManaBurstEntity;

import java.util.List;

public class ExcaliburBurst extends SpecializedBurstModifier implements ModifyBurstModifierHook, UpdateBurstModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, CtiBotModifierHooks.MODIFY_BURST,CtiBotModifierHooks.UPDATE_BURST);
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burst.entity().setDeltaMovement(burst.entity().getDeltaMovement().scale(1.5));
        burst.setColor(0xFFFF20);
        burst.setMana(burst.getMana()+100);
        burstExtras.addBaseDamage(16);
        burstExtras.addEntityPerConsumption(50);
    }

    @Override
    public void updateBurst(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst,IManaBurstExtra burstExtra) {
        var entity = EntityInRangeUtil.getNearestLivingEntity(burst.entity(),8,((IManaBurstExtra)burst).cti$getHitEntityIdList(), entity1 -> canHitEntity(entity1, (ManaBurstEntity) burst));
        if (entity!=null) ProjectileUtil.homingToward(burst.entity(),entity,1,4);
    }

    protected static boolean canHitEntity(Entity pTarget, ManaBurstEntity entity) {
        if (pTarget==entity.getOwner()) return false;
        if (((IManaBurstExtra)entity).cti$getHitEntityIdList().contains(pTarget.getId())) return false;
        if (pTarget instanceof ItemEntity ||pTarget instanceof ExperienceOrb) return false;
        if (pTarget instanceof Projectile) return false;
        return !(pTarget instanceof Player);
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return DynamicComponentUtil.scrollColorfulText.getColorfulText(getTranslationKey(),null,
                new int[]{0xFFBA43,0xFFFA71,0xFFFCB3},4,150,true);
    }
}
