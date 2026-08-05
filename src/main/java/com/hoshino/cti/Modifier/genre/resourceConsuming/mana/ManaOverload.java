package com.hoshino.cti.Modifier.genre.resourceConsuming.mana;

import com.hoshino.cti.Modifier.genre.insatiable.BasicInsatiableModifier;
import com.hoshino.cti.Modifier.genre.insatiable.forTrait.InsatiableHandler;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.BurstHitModifierHook;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ToolType;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.impl.mana.ManaItemHandlerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ManaOverload extends BasicInsatiableModifier implements ModifyBurstModifierHook, BurstHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(CtiModifiers.OVERSLIME_HANDLER.getId(),1,true));
        builder.addHook(this, CtiBotModifierHooks.MODIFY_BURST,CtiBotModifierHooks.BURST_HIT);
    }
    @Override
    public int getInsatiableLevel() {
        return 4;
    }

    @Override
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier) {
        return 32;
    }

    @Override
    public void burstLaunch(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, Player player, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        InsatiableHandler.applyEffect(player, ToolType.MELEE,tool.getModifierLevel(CtiModifiers.INSATIABLE_HANDLER.getId()));
    }

    @Override
    public void afterBurstHitEntity(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull LivingEntity target, ManaBurst burst, IManaBurstExtra burstExtra, float damage) {
        if (tool!=null&&owner instanceof Player player&&player.level instanceof ServerLevel serverLevel&&!burst.entity().getTags().contains("mana_overload_tag")){
            burst.entity().addTag("mana_overload_tag");
            var source = new EntityDamageSource("cti_mana",owner).bypassArmor().bypassMagic();
            var slashDamage = damage*0.1f;
            slashDamage = CtiModifiers.OVERSLIME_HANDLER.get().getMeleeDamage(tool,modifier,new ToolAttackContext(player,player, InteractionHand.MAIN_HAND,target,target,false,1,true),slashDamage,slashDamage);
            serverLevel.sendParticles(CtiParticleType.MANA_STRIKE.get(),target.getX(),target.getY()+0.5*target.getBbHeight(),target.getZ(),1,0,0,0,0);
            target.invulnerableTime = 0;
            target.hurt(source, slashDamage);
        }
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (!(context.getTarget() instanceof LivingEntity living)) return;
        var attacker = context.getAttacker();
        var atomicLv = new AtomicInteger();
        List.of(ToolType.MELEE, ToolType.ARMOR,ToolType.RANGED).forEach(toolType -> {
            var insatiable = attacker.getEffect(TinkerModifiers.insatiableEffect.get(toolType));
            if (insatiable!=null)
                atomicLv.addAndGet(insatiable.getAmplifier()+1);
        });
        int insatiableLv = atomicLv.get();
        if (insatiableLv>0&&attacker instanceof Player player&&player.level instanceof ServerLevel serverLevel){
            if (ManaItemHandlerImpl.INSTANCE.requestManaExactForTool(((ToolStack)tool).createStack(),player, (int) Math.floor(insatiableLv*0.1f),true)){
                serverLevel.sendParticles(CtiParticleType.MANA_STRIKE.get(),living.getX(),living.getY()+0.5*living.getBbHeight(),living.getZ(),1,0,0,0,0);
                living.invulnerableTime = 0;
                living.hurt(new EntityDamageSource("cti_mana",player).bypassArmor(), (float) insatiableLv);
            }
        }
    }

    @Override
    public List<Component> getDescriptionList() {
        var list =new ArrayList<>(super.getDescriptionList());
        list.add(Component.translatable("info.cti.overslime"));
        list.add(Component.translatable("info.cti.manaburst"));
        return list;
    }
}
