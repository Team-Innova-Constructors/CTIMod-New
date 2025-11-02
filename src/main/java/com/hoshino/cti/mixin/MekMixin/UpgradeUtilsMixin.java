package com.hoshino.cti.mixin.MekMixin;

import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(remap = false, value = UpgradeUtils.class)
public class UpgradeUtilsMixin {
    /**
     * @author EtSH_C2H6S
     * @reason 让信息正确显示倍率
     */
    @Overwrite
    public static List<Component> getMultScaledInfo(IUpgradeTile tile, Upgrade upgrade) {
        List<Component> ret = new ArrayList<>();
        if (tile.supportsUpgrades() && upgrade.getMax() > 1) {
            double effect = Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(), (float) tile.getComponent().getUpgrades(upgrade) / (float) 8);
            ret.add(MekanismLang.UPGRADES_EFFECT.translate(Math.round(effect * 100) / 100F));
        }
        return ret;
    }

    /**
     * @author EtSH_C2H6S
     * @reason 让信息正确显示倍率
     */
    @Overwrite
    public static List<Component> getExpScaledInfo(IUpgradeTile tile, Upgrade upgrade) {
        List<Component> ret = new ArrayList<>();
        if (tile.supportsUpgrades() && upgrade.getMax() > 1) {
            ret.add(MekanismLang.UPGRADES_EFFECT.translate(Math.pow(2, (float) tile.getComponent().getUpgrades(upgrade) / 8d)));
        }
        return ret;
    }
}
