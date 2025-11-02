package com.hoshino.cti.Entity.Projectiles;

import com.c2h6s.etshtinker.util.attackUtil;
import com.hoshino.cti.Entity.DisposibleFakePlayer;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PRailgunItemS2C;
import com.mojang.authlib.GameProfile;
import com.xiaoyue.tinkers_ingenuity.content.tools.definition.ToolDefinitions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;
import java.util.UUID;

public class TinkerRailgunProjectile extends AbstractArrow {
    public final MaterialNBT materialNBT;
    public final ToolStack tool;
    public ItemStack stack;
    public float damageMul = 40;

    public TinkerRailgunProjectile(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_, ItemStack itemStack, ModifiableItem item) {
        super(p_36721_, p_36722_);
        stack = itemStack;
        ToolPartItem toolPartItem = (ToolPartItem) itemStack.getItem();
        if (item == TinkerTools.cleaver.get()) {
            materialNBT = new MaterialNBT(List.of(MaterialVariant.of(toolPartItem.getMaterial(itemStack)), MaterialVariant.of(toolPartItem.getMaterial(itemStack)), MaterialVariant.of(toolPartItem.getMaterial(itemStack)), MaterialVariant.of(toolPartItem.getMaterial(itemStack))));
        } else
            materialNBT = new MaterialNBT(List.of(MaterialVariant.of(toolPartItem.getMaterial(itemStack)), MaterialVariant.of(toolPartItem.getMaterial(itemStack)), MaterialVariant.of(toolPartItem.getMaterial(itemStack))));
        tool = ToolStack.createTool(item, ToolDefinitions.RAPIER, materialNBT);
    }

    @Override
    public boolean isNoGravity() {
        return this.tickCount <= 50;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 1200) {
            this.discard();
        }
    }

    public void sendItemS2CPacket() {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            CtiPacketHandler.sendToClient(new PRailgunItemS2C(this, stack));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity at = this.getOwner();
        if (at instanceof Player player && this.tool != null && player.level instanceof ServerLevel serverLevel) {
            entity.invulnerableTime = 0;
            if (tool.getItem() == TinkerTools.cleaver.get()) {
                tool.addModifier(TinkerModifiers.severing.getId(), 18);
            }
            tool.rebuildStats();
            FakePlayer fakePlayer = new DisposibleFakePlayer(serverLevel, new GameProfile(UUID.randomUUID(), player.getName().getString()));
            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, tool.createStack());
            fakePlayer.setPos(player.getX(), player.getY(), player.getZ());
            fakePlayer.setDeltaMovement(player.getDeltaMovement());
            fakePlayer.setHealth(player.getHealth());
            fakePlayer.setExperienceLevels(player.experienceLevel);
            fakePlayer.setItemSlot(EquipmentSlot.CHEST, player.getItemBySlot(EquipmentSlot.CHEST));
            fakePlayer.setItemSlot(EquipmentSlot.HEAD, player.getItemBySlot(EquipmentSlot.HEAD));
            fakePlayer.setItemSlot(EquipmentSlot.LEGS, player.getItemBySlot(EquipmentSlot.LEGS));
            fakePlayer.setItemSlot(EquipmentSlot.FEET, player.getItemBySlot(EquipmentSlot.FEET));
            fakePlayer.setXRot(player.getXRot());
            fakePlayer.setYRot(player.getYRot());
            fakePlayer.setYHeadRot(player.getYHeadRot());
            ToolAttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, entity, () -> 1, true);
            entity.invulnerableTime = 0;
            attackUtil.attackEntity(tool, fakePlayer, InteractionHand.MAIN_HAND, entity, () -> 1, false, EquipmentSlot.MAINHAND, tool.getStats().get(ToolStats.ATTACK_DAMAGE) * damageMul, true, true, true, true, 0);
            entity.invulnerableTime = 0;
        }
        super.onHitEntity(result);
    }

    @Override
    protected ItemStack getPickupItem() {
        return stack;
    }
}
