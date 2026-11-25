package com.hoshino.cti.mixin.Eni;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.CursedRing;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.hoshino.cti.Modifier.Race.DemonBloodline;
import com.hoshino.cti.client.cache.SevenCurse;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.ServerCursePacket;
import com.hoshino.cti.util.CurseUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = CursedRing.class, remap = false)
public abstract class CurseRingMixin extends ItemBaseCurio {
    @Shadow
    public static Omniconfig.PerhapsParameter painMultiplier;

    @Shadow
    public static Omniconfig.PerhapsParameter armorDebuff;

    @Shadow
    public static Omniconfig.PerhapsParameter monsterDamageDebuff;

    @Shadow
    public static Omniconfig.IntParameter lootingBonus;

    @Shadow
    public static Omniconfig.IntParameter fortuneBonus;

    @Shadow
    public static Omniconfig.PerhapsParameter experienceBonus;

    @Shadow
    public static Omniconfig.IntParameter enchantingBonus;

    @Shadow
    public static Omniconfig.BooleanParameter enableLore;

    @Shadow
    public static Omniconfig.DoubleParameter neutralAngerRange;

    @Shadow
    public static Omniconfig.DoubleParameter neutralXRayRange;

    @Shadow
    public static Omniconfig.DoubleParameter endermenRandomportRange;

    @Shadow
    public static Omniconfig.DoubleParameter endermenRandomportFrequency;

    /**
     * @author
     * @reason 在原版七咒之戒的 curioTick 基础上整合惩罚时间/坚毅层数逻辑、取消末影人强制传送
     * 并新增"恶魔血统"词条判定：携带该词条时不执行强制仇恨。
     */
    @Overwrite
    public void curioTick(SlotContext context, ItemStack stack) {
        if (!(context.entity() instanceof Player player)) return;

        var data = stack.getOrCreateTag();
        int time = CurseUtil.getPunishTime(player);
        if (time > 0) {
            if (player.tickCount % 20 == 0) {
                data.putInt("punish_time", time - 1);
            }
        } else {
            Level worldIn = player.getLevel();
            long worldTime = worldIn.getGameTime();
            int resoluteTime = data.getInt("resolute");
            if (worldTime % 24000 == 0) {
                data.putInt("resolute", Math.min(3, resoluteTime + 1));
            }
        }
        if (player instanceof ServerPlayer serverPlayer) {
            int punishTime = CurseUtil.getPunishTime(player);
            int deathFre = CurseUtil.getDeathFrequency(player);
            int resoluteTime = CurseUtil.getResoluteTime(player);
            CtiPacketHandler.sendToPlayer(new ServerCursePacket(punishTime, deathFre, resoluteTime), serverPlayer);
        }

        if (DemonBloodline.hasBloodline(player)) return;

        if (player.isCreative() || player.isSpectator()) return;

        Level level = player.getLevel();

        for (EnderMan enderman : level.getEntitiesOfClass(EnderMan.class,
                SuperpositionHandler.getBoundingBoxAroundEntity(player, endermenRandomportRange.getValue()))) {
            if (!(random.nextDouble() <= 0.002 * endermenRandomportFrequency.getValue())
                    || !player.hasLineOfSight(enderman)) continue;
            enderman.setTarget(player);
        }

        if (!SuperpositionHandler.hasCurio(player, EnigmaticItems.AVARICE_SCROLL)) {
            for (Mob mob : level.getEntitiesOfClass(Mob.class,
                    SuperpositionHandler.getBoundingBoxAroundEntity(player, neutralAngerRange.getValue()))) {
                if (!(mob instanceof Piglin) && !(mob instanceof ZombifiedPiglin)) continue;

                double visibility = player.getVisibilityPercent(mob);
                double angerDistance = Math.max(neutralAngerRange.getValue() * visibility, neutralXRayRange.getValue());
                if (mob.distanceToSqr(player.getX(), player.getY(), player.getZ()) > angerDistance * angerDistance) continue;
                if (mob.getTarget() != null && mob.getTarget().isAlive()) continue;
                if (!player.hasLineOfSight(mob) && (double) player.distanceTo(mob) > neutralXRayRange.getValue()) continue;

                if (mob instanceof Piglin piglin) {
                    PiglinAiAccessor.cti$wasHurtBy(piglin, player);
                } else {
                    ZombifiedPiglin zombifiedPiglin = (ZombifiedPiglin) mob;
                    zombifiedPiglin.setPersistentAngerTarget(player.getUUID());
                    zombifiedPiglin.startPersistentAngerTimer();
                    zombifiedPiglin.setTarget(player);
                }
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite()
    public void m_7373_(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
        ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
        if (Screen.hasShiftDown()) {
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing3");
            if (painMultiplier.getValue().asMultiplier() == (double) 2.0F) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing4");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing4_alt", ChatFormatting.GOLD, painMultiplier + "%");
            }
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing5");
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing6", ChatFormatting.GOLD, armorDebuff + "%");
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing7", ChatFormatting.GOLD, monsterDamageDebuff + "%");
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing8");
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing9");
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing10");
            var player = Minecraft.getInstance().player;
            if (player != null) {
                int time = SevenCurse.getPunishTime();
                int frequency = SevenCurse.getPunishFre();
                int resoluteTime = SevenCurse.getResoluteTime();
                var attackWeakValue = Math.round((1 - (1f / Math.max(frequency - 2, 1))) * 1000) / 10;
                var string1 = "当前你已经累计死亡" + (frequency) + "次";
                var string2 = "由于你的死亡,你受到的伤害额外增加" + Math.max((frequency - 3) * 0.5f * 100, 0) + "%,造成的伤害降低" + attackWeakValue + "%";
                var string3 = "在" + (time) + "秒后,会结束灵魂破碎对你的影响";
                var string7 = "灵魂破碎影响已结束，暂时的";

                var string4 = "当前坚毅层数为" + (resoluteTime) + "层,会减少受到的" + resoluteTime * 6 + "%" + "伤害";
                var string5 = "如果你死亡了会清除所有的坚毅层数!";
                long resoluteWait = 24000 - player.getLevel().getGameTime() % 24000;
                var string6 = "下一层坚毅层数续上倒计时" + resoluteWait;
                list.add(Component.literal(string1).withStyle(style -> style.withColor(0xff435c)));
                list.add(Component.literal(string2).withStyle(style -> style.withColor(0xff435c)));
                if (time > 0) {
                    list.add(Component.literal(string3).withStyle(style -> style.withColor(0xff435c)));
                } else {
                    list.add(Component.literal(string7).withStyle(style -> style.withColor(0xff435c)));
                }
                list.add(Component.literal(string4).withStyle(style -> style.withColor(0xffaa7f)));
                list.add(Component.literal(string5).withStyle(style -> style.withColor(0xffaa7f)));
                if (resoluteTime < 3) {
                    if (time > 0) {
                        list.add(Component.literal("在你的死亡惩罚结束前无法获得坚毅层数!").withStyle(style -> style.withColor(0xff435c)));
                    } else
                        list.add(Component.literal(string6).withStyle(style -> style.withColor(0xffaa7f)));
                }
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing11");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing12", ChatFormatting.GOLD, lootingBonus.getValue() - 1 + resoluteTime);
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing13", ChatFormatting.GOLD, fortuneBonus.getValue() - 1 + resoluteTime);
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing14", ChatFormatting.GOLD, experienceBonus + "%");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing15", ChatFormatting.GOLD, enchantingBonus);
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing16");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing17");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing18");
            }
        } else {
            if (enableLore.getValue()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore1");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore2");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore3");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore4");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore5");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore6");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore7");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
            }

            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eternallyBound1");
            if (Minecraft.getInstance().player != null && SuperpositionHandler.canUnequipBoundRelics(Minecraft.getInstance().player)) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eternallyBound2_creative");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eternallyBound2");
            }

            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
        }
    }

    @Inject(method = "getFortuneLevel", at = @At("RETURN"), cancellable = true)
    private void fortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack curio, CallbackInfoReturnable<Integer> cir) {
        var resoluteTime = curio.getOrCreateTag().getInt("resolute");
        cir.setReturnValue(super.getFortuneLevel(slotContext, lootContext, curio) + fortuneBonus.getValue() - 1 + resoluteTime);
    }

    @Inject(method = "getLootingLevel", at = @At("RETURN"), cancellable = true)
    private void lootLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack curio, CallbackInfoReturnable<Integer> cir) {
        var resoluteTime = curio.getOrCreateTag().getInt("resolute");
        cir.setReturnValue(super.getLootingLevel(slotContext, source, target, baseLooting, curio) + lootingBonus.getValue() - 1 + resoluteTime);
    }
}
