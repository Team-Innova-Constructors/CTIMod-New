package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.hoshino.cti.Entity.Projectiles.VoidArcEntity;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.forTrait.OverslimeHandler;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

public class UnitedOverslime extends BasicOverslimeModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(TinkerModifiers.overslime.getId(),1,true));
    }

    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return 5*modifier.getLevel();
    }

    @Override
    public float getDamageMul(IToolContext context, ModifierEntry modifier) {
        return 0.25f*modifier.getLevel();
    }

    @Override
    public int getPriority() {
        return OverslimeHandler.OVERSLIME_MODIFIER_PRIORITY-1;
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.modifierAddToolStats(context,modifier,builder);
        OverslimeModifier.OVERSLIME_STAT.percent(builder,0.2f*modifier.getLevel());
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isFullyCharged()){
            var attacker = context.getAttacker();
            var level = attacker.level;
            var target = context.getTarget();
            var targetCenter = target.position().add(0, 0.5 * target.getBbHeight(), 0);
            var os = TinkerModifiers.overslime.get();
            if (os.getShield(tool)>=20*modifier.getLevel()){
                os.addOverslime(tool,modifier,-20*modifier.getLevel());
                VoidArcEntity entity = new VoidArcEntity(level);
                entity.setPos(targetCenter);
                entity.setRadius(1f+0.5f*modifier.getLevel());
                entity.setDamage(0.05f*damage*modifier.getLevel());
                entity.setLvl(modifier.getLevel()+1);
                entity.setOwner(attacker);
                entity.setHomingEntity(target);
                level.addFreshEntity(entity);
            }
        }
    }
}
