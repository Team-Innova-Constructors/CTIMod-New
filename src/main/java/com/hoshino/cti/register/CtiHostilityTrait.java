package com.hoshino.cti.register;

import com.hoshino.cti.L2.*;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.ChatFormatting;

public class CtiHostilityTrait {
    public static final RegistryEntry<PurifyTrait> PURIFYTRAIT = L2Hostility.REGISTRATE.regTrait("purify", () -> new PurifyTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 100, 30, 1, 400)).desc("The Mob who have this trait will ignore Harmful effects and increase 10% damage each trait level").lang("Purify").register();
    public static final RegistryEntry<OracleTrait> ORACLE_TRAIT = L2Hostility.REGISTRATE.regTrait("oracle", () -> new OracleTrait(ChatFormatting.GREEN), (rl) -> new TraitConfig(rl, 100, 30, 1, 1)).desc("The Mob who have this trait will clear positive effects of targets attacked.").lang("Oracle").register();
    public static final RegistryEntry<ExtremeDamageReduce> EXTREME_DAMAGE_REDUCE = L2Hostility.REGISTRATE.regTrait("extreme_damage_reduce", () -> new ExtremeDamageReduce(ChatFormatting.GREEN), (rl) -> new TraitConfig(rl, 100, 30, 1, 10000)).desc("Grant 60% damage reduction to Extreme Damage, 80% damage reduction to bypass armor damage.").lang("Extreme DR").register();
    public static final RegistryEntry<TemporaryArmor> TEMPORARY_ARMOR = L2Hostility.REGISTRATE.regTrait("temporary_armor", () -> new TemporaryArmor(ChatFormatting.GRAY), (rl) -> new TraitConfig(rl, 200, 5, 1, 750)).desc("Grant 99% damage reduction but decrease 1% when hit. Bypass armor damage will result in 5x effectiveness.").lang("Temporary Reduction").register();
    public static final RegistryEntry<SecondPhase> SECOND_PHASE = L2Hostility.REGISTRATE.regTrait("second_phase", () -> new SecondPhase(ChatFormatting.GOLD), (rl) -> new TraitConfig(rl, 200, 10, 3, 750)).desc("Grant %s seconds of invulnerable time when health drops over 50%.").lang("Second Phase").register();

    public static void register() {
    }
}
