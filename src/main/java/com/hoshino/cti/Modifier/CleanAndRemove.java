package com.hoshino.cti.Modifier;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CleanAndRemove extends Modifier implements MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getTarget() instanceof LivingEntity living && context.isFullyCharged() && !living.level.isClientSide()) {
            if(tool.getModifierLevel(new ModifierId("tinkersinnovation:miracle"))>0){
                living.getCapability(MobTraitCap.CAPABILITY).ifPresent(cap -> {
                    Set<MobTrait> set = cap.traits.keySet();
                    if (set.isEmpty()) {
                        return;
                    }
                    var random = living.getRandom();
                    var data=living.getPersistentData();
                    int ti=data.getInt("ragnarok_drop");
                    if (random.nextInt(Math.max(1,10-modifier.getLevel())) == 0&&ti<2) {
                        List<MobTrait> traitList = new ArrayList<>(set);
                        MobTrait trait = traitList.get(random.nextInt(traitList.size()));
                        if (trait != null) {
                            int count = cap.traits.getOrDefault(trait, 1);
                            cap.removeTrait(trait);
                            cap.syncToClient(living);
                            ItemStack dropStack = new ItemStack(trait.asItem(), count);
                            ItemEntity entity = new ItemEntity(living.level, living.getX(), living.getY(), living.getZ(), dropStack);
                            entity.setDefaultPickUpDelay();
                            living.level.addFreshEntity(entity);
                            data.putInt("ragnarok_drop",ti+1);
                        }
                    }
                });
            }
        }
    }
    private static boolean hasMiracle(IToolStackView tool){
        var materialNBTList = tool.getMaterials().getList();
        var kemomimiId = new MaterialId("tinkersinnovation:miracle");
        for (MaterialVariant variant : materialNBTList) {
            if (variant.getId().equals(kemomimiId)) {
                return true;
            }
        }
        return false;
    }
}
