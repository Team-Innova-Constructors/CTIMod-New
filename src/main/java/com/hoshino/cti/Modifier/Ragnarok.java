package com.hoshino.cti.Modifier;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Random;
import java.util.Set;

public class Ragnarok extends NoLevelsModifier implements MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getTarget() instanceof LivingEntity living&&context.isFullyCharged()) {
            Random random = new Random();
            LazyOptional<MobTraitCap> optional = living.getCapability(MobTraitCap.CAPABILITY);
            if (optional.resolve().isPresent()) {
                MobTraitCap cap = optional.resolve().get();
                Set<MobTrait> set = cap.traits.keySet();
                MobTrait trait = set.stream().toList().get(random.nextInt(set.size()));
                int count = cap.traits.get(trait);
                if (trait != null && random.nextInt(3) == 0) {
                    cap.removeTrait(trait);
                    cap.syncToClient(living);
                    ItemEntity entity = new ItemEntity(living.level, living.getX(), living.getY(), living.getZ(), new ItemStack(trait.asItem(), count));
                    living.level.addFreshEntity(entity);
                }
            }
        }
    }
}
