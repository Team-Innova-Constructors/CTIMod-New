package com.hoshino.cti.Modifier;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class DeathRolling extends Modifier implements EntityInteractionModifierHook , InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ENTITY_INTERACT,ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(!(livingEntity instanceof Player player))return;
        if(player.tickCount%10!=0)return;
        List<Mob> mobList=player.level.getEntitiesOfClass(Mob.class,new AABB(player.getOnPos()).inflate(1),mob -> !(mob instanceof Villager)&&!(mob instanceof TamableAnimal));
        if(b){
            for(Mob mob:mobList){
                ToolAttackUtil.attackEntity(itemStack,player,mob);
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
        else {
            for(Mob mob:mobList){
                EntityDamageSource skd=new EntityDamageSource("skd",player);
                if(player.getMaxHealth() >80){
                    skd.bypassArmor();
                }
                mob.hurt(skd,player.getMaxHealth() * 0.2f);
            }
        }
    }
    @Override
    public @NotNull InteractionResult beforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, Entity target, InteractionHand hand, InteractionSource source) {

        if(!(target instanceof LivingEntity living))return InteractionResult.PASS;
        if(living instanceof Villager||living instanceof TamableAnimal)return InteractionResult.PASS;

        var targetPos=living.position();
        var playerPos=player.position();
        var sub=targetPos.subtract(playerPos);
        sub.add(new Vec3(0,0.1,0));

        ToolAttackUtil.attackEntity(player.getItemInHand(hand),player,living);
        player.setDeltaMovement(sub);
        player.swing(InteractionHand.OFF_HAND);

        return InteractionResult.SUCCESS;
    }
}
