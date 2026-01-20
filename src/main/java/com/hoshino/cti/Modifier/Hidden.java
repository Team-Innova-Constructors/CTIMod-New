package com.hoshino.cti.Modifier;

import com.hoshino.cti.Cti;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.ExposedUpdatePacket;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Hidden extends Modifier implements InventoryTickModifierHook , MeleeDamageModifierHook {
    private static final ResourceLocation KEMOMIMI_HIDDEN = Cti.getResource("kemimimi_hidden");
    private static final ResourceLocation HIDDEN_SUPER_HURT = Cti.getResource("hidden_super_hurt");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK,ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if (livingEntity instanceof Player player) {
            if (player.tickCount % 20 != 0) return;
            if (hasKemomimi(iToolStackView)) {
                int hiddenWaitTick = getHiddenWaitTick(iToolStackView);
                if (hiddenWaitTick > 0) {
                    setHiddenWaitTick(iToolStackView, hiddenWaitTick - 1);
                } else livingEntity.addEffect(new MobEffectInstance(CtiEffects.covert.get(), 200, 1, false, false,true));
            }
            else {
                if(player.tickCount%200==0){
                    livingEntity.addEffect(new MobEffectInstance(CtiEffects.covert.get(),100,1,false,false,true));
                }
            }
        }
    }

    public boolean hasKemomimi(IToolStackView view) {
        var materialNBTList = view.getMaterials().getList();
        var kemomimiId = new MaterialId("solidarytinker:kemomimi");
        for (MaterialVariant variant : materialNBTList) {
            if (variant.getId().equals(kemomimiId)) {
                return true;
            }
        }
        return false;
    }

    public int getHiddenWaitTick(IToolStackView view) {
        return view.getPersistentData().getInt(KEMOMIMI_HIDDEN);
    }

    public void setHiddenWaitTick(IToolStackView view, int amount) {
        view.getPersistentData().putInt(KEMOMIMI_HIDDEN, amount);
    }
    public int getSuperHurt(IToolStackView view) {
        return view.getPersistentData().getInt(HIDDEN_SUPER_HURT);
    }

    public void setSuperHurt(IToolStackView view, int amount) {
        view.getPersistentData().putInt(HIDDEN_SUPER_HURT, amount);
    }

    public static void breakHidden(IToolStackView view,Player player) {
        view.getPersistentData().putInt(KEMOMIMI_HIDDEN, 10);
        view.getPersistentData().putInt(HIDDEN_SUPER_HURT, 9);
        int index=player.getRandom().nextInt(4);
        if(player instanceof ServerPlayer serverPlayer){
            CtiPacketHandler.sendToPlayer(new ExposedUpdatePacket(index,20),serverPlayer);
        }
        player.level.playSound(null,player.getOnPos(), CtiSounds.location_exposed.get(), SoundSource.AMBIENT,1,1);
        player.removeEffect(CtiEffects.covert.get());
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float v, float v1) {
        if(!hasKemomimi(iToolStackView))return v1;
        var player=toolAttackContext.getPlayerAttacker();
        if(player==null)return v1;
        if(player.hasEffect(CtiEffects.covert.get())){
            breakHidden(iToolStackView,player);
        }
        if(getHiddenWaitTick(iToolStackView)<10){
            setHiddenWaitTick(iToolStackView,10);
        }
        int superHurt=getSuperHurt(iToolStackView);
        if(superHurt>0){
            setSuperHurt(iToolStackView,getSuperHurt(iToolStackView) - 1);
            return v1 * 3;
        }
        return v1;
    }
}
