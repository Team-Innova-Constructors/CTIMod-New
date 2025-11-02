package com.hoshino.cti.Modifier.Developer;

import com.c2h6s.etshtinker.util.slotUtil;
import com.hoshino.cti.Entity.Projectiles.FallenStars;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;
import static com.c2h6s.etshtinker.util.vecCalc.getScatteredVec3;

public class All extends NoLevelsModifier implements ToolDamageModifierHook, ToolStatsModifierHook {
    public static List<MobEffect> ls = new ArrayList<>(List.of());

    public static void init() {
        Iterator<Potion> iterator = ForgeRegistries.POTIONS.iterator();
        if (iterator.hasNext()) {
            Potion potion = iterator.next();
            potion.getEffects().forEach((mobEffectInstance -> {
                if (mobEffectInstance.getEffect().getCategory()==MobEffectCategory.HARMFUL) ls.add(mobEffectInstance.getEffect());
            }));
        }
    }

    public All() {
        MinecraftForge.EVENT_BUS.addListener(this::livingHurtEvent);
        MinecraftForge.EVENT_BUS.addListener(this::livingAttackEvent);
    }

    private void livingAttackEvent(LivingAttackEvent event) {
        if (ls.isEmpty()) init();
        if (event.getEntity() instanceof Player player) {
            for (EquipmentSlot slot : slotUtil.ALL) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(this) > 0) {
                        event.setCanceled(true);
                        StellarBlade.summonStars(player);
                        if (event.getAmount() > 5 && slotUtil.ARMOR.contains(slot) && !player.getCooldowns().isOnCooldown(tool.getItem())) {
                            List<Mob> mobbbbbbb = player.level.getEntitiesOfClass(Mob.class, new AABB(player.getX() - 16, player.getY() - 16, player.getZ() - 16, player.getX() + 16, player.getY() + 16, player.getZ() + 16));
                            if (!mobbbbbbb.isEmpty()) {
                                for (Mob target : mobbbbbbb) {
                                    if (target != null) {
                                        int c = 0;
                                        while (c < EtSHrnd().nextInt(3) + 1) {
                                            Level level = player.level;
                                            Vec3 vec3 = getScatteredVec3(new Vec3(0, -0.25, 0), 0.57735);
                                            double d = EtSHrnd().nextDouble() * 20;
                                            Vec3 direction = new Vec3(-(30 + d) * vec3.x, -(30 + d) * vec3.y, -(30 + d) * vec3.z);
                                            FallenStars fallenStars;
                                            int rnd = EtSHrnd().nextInt(4);
                                            if (rnd == 1) {
                                                fallenStars = new FallenStars(CtiEntity.star_pressure.get(), level, StellarBlade.ls.get(1));
                                            } else if (rnd == 2) {
                                                fallenStars = new FallenStars(CtiEntity.star_ionize.get(), level, StellarBlade.ls.get(2));
                                            } else if (rnd == 3) {
                                                fallenStars = new FallenStars(CtiEntity.star_frozen.get(), level, StellarBlade.ls.get(3));
                                            } else {
                                                fallenStars = new FallenStars(CtiEntity.star_blaze.get(), level, StellarBlade.ls.get(0));
                                            }
                                            fallenStars.setOwner(player);
                                            fallenStars.baseDamage = event.getAmount();
                                            fallenStars.setDeltaMovement(vec3);
                                            fallenStars.setPos(target.getX() + direction.x, target.getY() + target.getBbHeight() + direction.y, target.getZ() + direction.z);
                                            player.level.addFreshEntity(fallenStars);
                                            c++;
                                        }
                                    }
                                }
                                player.getCooldowns().addCooldown(tool.getItem(), 10);
                            }
                        }
                        if (event.getSource().getEntity() instanceof LivingEntity living && !(living instanceof Player)) {
                            CompoundTag tag = living.getPersistentData();
                            tag.putBoolean("vulnerable", true);
                            if (!tag.contains("dmg_amplifier")) {
                                tag.putFloat("dmg_amplifier", 1.5f);
                            } else {
                                tag.putFloat("dmg_amplifier", Math.max(1.5f, tag.getFloat("dmg_amplifier") + 0.5f));
                            }

                            if (!tag.contains("legacyhealth")) {
                                tag.putFloat("legacyhealth", living.getHealth() - event.getAmount());
                            } else {
                                if (living.getHealth() > tag.getFloat("legacyhealth")) {
                                    living.setHealth(tag.getFloat("legacyhealth"));
                                }
                                tag.putFloat("legacyhealth", tag.getFloat("legacyhealth") - event.getAmount());
                            }

                            if (!tag.contains("atomic_dec")) {
                                tag.putFloat("atomic_dec", 20f);
                            } else {
                                tag.putFloat("atomic_dec", Math.max(20, tag.getFloat("atomic_dec") + 20));
                            }

                            if (!tag.contains("quark_disassemble")) {
                                tag.putFloat("quark_disassemble", 20f);
                            } else {
                                tag.putFloat("quark_disassemble", Math.max(20, tag.getFloat("quark_disassemble") + 20));
                            }
                            if (ls != null && !ls.isEmpty()) {
                                int i = 0;
                                while (i < 10) {
                                    MobEffect effect = ls.get(EtSHrnd().nextInt(ls.size()));
                                    living.forceAddEffect(new MobEffectInstance(effect, 200, 9, false, false), player);
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE, ModifierHooks.TOOL_STATS);
    }

    private void livingHurtEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player && !(event.getEntity() instanceof Player)) {
            for (EquipmentSlot slot : slotUtil.ALL) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.getItem() instanceof IModifiable) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(this) > 0 && event.getEntity() != null && !(event.getEntity() instanceof Player)) {
                        event.setAmount(event.getAmount() * 10);
                        CompoundTag tag = event.getEntity().getPersistentData();
                        tag.putBoolean("vulnerable", true);
                        if (!tag.contains("dmg_amplifier")) {
                            tag.putFloat("dmg_amplifier", 1.5f);
                        } else {
                            tag.putFloat("dmg_amplifier", Math.max(1.5f, tag.getFloat("dmg_amplifier") + 0.5f));
                        }

                        if (!tag.contains("legacyhealth")) {
                            tag.putFloat("legacyhealth", event.getEntity().getHealth() - event.getAmount());
                        } else {
                            if (event.getEntity().getHealth() > tag.getFloat("legacyhealth")) {
                                event.getEntity().setHealth(tag.getFloat("legacyhealth"));
                            }
                            tag.putFloat("legacyhealth", tag.getFloat("legacyhealth") - event.getAmount());
                        }

                        if (!tag.contains("atomic_dec")) {
                            tag.putFloat("atomic_dec", 20f);
                        } else {
                            tag.putFloat("atomic_dec", Math.max(20, tag.getFloat("atomic_dec") + 20));
                        }

                        if (!tag.contains("quark_disassemble")) {
                            tag.putFloat("quark_disassemble", 20f);
                        } else {
                            tag.putFloat("quark_disassemble", Math.max(20, tag.getFloat("quark_disassemble") + 20));
                        }
                        if (ls != null && !ls.isEmpty()) {
                            int i = 0;
                            while (i < 10) {
                                MobEffect effect = ls.get(EtSHrnd().nextInt(ls.size()));
                                event.getEntity().forceAddEffect(new MobEffectInstance(effect, 200, 9, false, false), player);
                                i++;
                            }
                        }
                    }
                }
            }
            LazyOptional<IItemHandlerModifiable> optional = CuriosApi.getCuriosHelper().getEquippedCurios(player);
            if (optional.isPresent()) {
                IItemHandlerModifiable handler = optional.orElse(null);
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (stack.getItem() instanceof IModifiable) {
                        ToolStack tool = ToolStack.from(stack);
                        if (tool.getModifierLevel(this) > 0) {
                            event.setAmount(event.getAmount() * 10);
                            if (tool.getModifierLevel(this) > 0 && event.getEntity() != null && !(event.getEntity() instanceof Player)) {
                                CompoundTag tag = event.getEntity().getPersistentData();
                                tag.putBoolean("vulnerable", true);
                                if (!tag.contains("dmg_amplifier")) {
                                    tag.putFloat("dmg_amplifier", 1.5f);
                                } else {
                                    tag.putFloat("dmg_amplifier", Math.max(1.5f, tag.getFloat("dmg_amplifier") + 0.5f));
                                }

                                if (!tag.contains("legacyhealth")) {
                                    tag.putFloat("legacyhealth", event.getEntity().getHealth() - event.getAmount());
                                } else {
                                    if (event.getEntity().getHealth() > tag.getFloat("legacyhealth")) {
                                        event.getEntity().setHealth(tag.getFloat("legacyhealth"));
                                    }
                                    tag.putFloat("legacyhealth", tag.getFloat("legacyhealth") - event.getAmount());
                                }

                                if (!tag.contains("dmg_amplifier")) {
                                    tag.putFloat("dmg_amplifier", 1.5f);
                                } else {
                                    tag.putFloat("atomic_dec", Math.max(20, tag.getFloat("atomic_dec") + 20));
                                }

                                if (!tag.contains("dmg_amplifier")) {
                                    tag.putFloat("dmg_amplifier", 1.5f);
                                } else {
                                    tag.putFloat("quark_disassemble", Math.max(20, tag.getFloat("quark_disassemble") + 20));
                                }
                                if (ls != null && !ls.isEmpty()) {
                                    int j = 0;
                                    while (j < 10) {
                                        MobEffect effect = ls.get(EtSHrnd().nextInt(ls.size()));
                                        event.getEntity().forceAddEffect(new MobEffectInstance(effect, 200, 9, false, false), player);
                                        j++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getPriority() {
        return 512;
    }

    @Override
    public int onDamageTool(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i, @Nullable LivingEntity livingEntity) {
        return 0;
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        CtiToolStats.ELECTRIC_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.SCORCH_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.FROZEN_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.PRESSURE_RESISTANCE.add(modifierStatsBuilder, 50);
    }
}
