package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2library.util.code.GenericItemStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.modifiers.ModifierId;

@Mixin(value = TraitLootModifier.class,remap = false)
public abstract class TraitLootModifierMixin extends LootModifier {
    @Shadow @Final @Nullable public MobTrait trait;

    @Shadow @Final public double chance;

    @Shadow @Final public double rankBonus;

    @Shadow @Final public ItemStack result;

    protected TraitLootModifierMixin(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
        Object cap = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (cap instanceof LivingEntity le) {
            if (MobTraitCap.HOLDER.isProper(le)) {
                MobTraitCap mobCap = MobTraitCap.HOLDER.get(le);
                if (this.trait == null || mobCap.hasTrait(this.trait)) {
                    double factor = mobCap.dropRate;
                    Player player;
                    if (context.hasParam(LootContextParams.KILLER_ENTITY)) {
                        var entity = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
                        if (!(entity instanceof Player player1)) return list;
                        player = player1;

                        PlayerDifficulty pl = PlayerDifficulty.HOLDER.get(player);
                        for (GenericItemStack<CurseCurioItem> stack : CurseCurioItem.getFromPlayer(player)) {
                            factor *= stack.item().getLootFactor(stack.stack(), pl, mobCap);
                        }
                        int lv = this.trait == null ? 0 : mobCap.getTraitLevel(this.trait);
                        double rate = this.chance + (double) lv * this.rankBonus;
                        if (GetModifierLevel.EquipHasModifierlevel(player, new ModifierId("solidarytinker:littlecat"))) {
                            rate *= 2.5;
                        }
                        int count = 0;
                        double totalAttempts = (double) this.result.getCount() * factor;
                        int guaranteedAttempts = (int) totalAttempts;
                        double extraChance = totalAttempts - guaranteedAttempts;
                        for (int i = 0; i < guaranteedAttempts; ++i) {
                            if (context.getRandom().nextFloat() < rate) {
                                ++count;
                            }
                        }
                        if (extraChance > 0.0 && context.getRandom().nextFloat() < extraChance) {
                            if (context.getRandom().nextFloat() < rate) {
                                ++count;
                            }
                        }
                        if (count > 0) {
                            if (GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())) {
                                count *= 4;
                            }
                        }
                        if (count > 0) {
                            ItemStack ans = this.result.copy();
                            ans.setCount(count);
                            list.add(ans);
                        }
                    }
                }
            }
        }
        return list;
    }
}
