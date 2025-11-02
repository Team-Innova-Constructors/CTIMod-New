package com.hoshino.cti.mixin.IFMixin;

import com.buuz135.industrial.block.agriculturehusbandry.tile.MobCrusherTile;
import com.buuz135.industrial.block.tile.IndustrialWorkingTile;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Deprecated
@Mixin(value = MobCrusherTile.class, remap = false)
public abstract class crusherMixin {
    @Shadow
    protected abstract IndustrialWorkingTile<MobCrusherTile>.WorkAction damage(Mob entity, FakePlayer player);

    @Shadow
    protected abstract IndustrialWorkingTile<MobCrusherTile>.WorkAction instantKill(Mob entity, FakePlayer player);

    @Shadow
    public abstract IndustrialWorkingTile<MobCrusherTile>.WorkAction work();

    @Redirect(method = "work", at = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/block/agriculturehusbandry/tile/MobCrusherTile;instantKill(Lnet/minecraft/world/entity/Mob;Lnet/minecraftforge/common/util/FakePlayer;)Lcom/buuz135/industrial/block/tile/IndustrialWorkingTile$WorkAction;"), remap = false)
    private IndustrialWorkingTile<MobCrusherTile>.WorkAction worked(MobCrusherTile instance, Mob entity, FakePlayer player) {
        if (entity.getMaxHealth() <= 100) {
            return instantKill(entity, player);
        }
        return damage(entity, player);
    }
}
