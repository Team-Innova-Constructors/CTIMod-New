package com.hoshino.cti.mixin.Aquamixin;

import com.obscuria.aquamirae.common.ScrollEffects;
import com.obscuria.aquamirae.registry.AquamiraeItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ScrollEffects.class, remap = false)
public class ScrollEffectsMixin {
    @Shadow
    @Final
    private Player PLAYER;

    /**
     * @author firefly
     * @reason L2会直接导致玩家物品复制
     */
    @Overwrite
    private void mimic() {
        Drowned drowned = new Drowned(EntityType.DROWNED, this.PLAYER.level);
        if (this.PLAYER.getLevel() instanceof ServerLevel level) {
            drowned.finalizeSpawn(level, PLAYER.level.getCurrentDifficultyAt(PLAYER.blockPosition()), MobSpawnType.EVENT, null, null);
            ItemStack[] armorslot = new ItemStack[]{
                    new ItemStack(AquamiraeItems.FIN_CUTTER.get()),
                    new ItemStack(AquamiraeItems.TERRIBLE_BOOTS.get()),
                    new ItemStack(AquamiraeItems.TERRIBLE_LEGGINGS.get()),
                    new ItemStack(AquamiraeItems.TERRIBLE_CHESTPLATE.get()),
                    new ItemStack(AquamiraeItems.TERRIBLE_HELMET.get()),
            };
            EquipmentSlot[] slots = EquipmentSlot.values();
            for (int i = 0; i < armorslot.length; i++) {
                if (i != 2) {
                    drowned.setItemSlot(slots[i], armorslot[i]);
                }
            }
            level.addFreshEntity(drowned);
            drowned.moveTo(PLAYER.position());
        }
    }
}
