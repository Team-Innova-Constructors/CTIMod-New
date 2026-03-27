package com.hoshino.cti.Event;

import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.hoshino.cti.Capabilitiess.PlayerEnigmaticItemsCondition;
import com.hoshino.cti.register.CtiLootCondition;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.hoshino.cti.Cti.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class EnigmaticSpellStoneLoot {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEniLoot(LootTableLoadEvent event){
        if (OmniconfigHandler.customDungeonLootEnabled.getValue()) {
            if (SuperpositionHandler.getMergedAir$EarthenDungeons().contains(event.getName())) {
                List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
                LootPoolEntryContainer.Builder<?> golemHeart = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.GOLEM_HEART, 65);
                if (golemHeart != null) {
                    entries.add(golemHeart.when(CtiLootCondition.playerL2Level(200)));
                }
                LootPoolEntryContainer.Builder<?> angelBlessing = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.ANGEL_BLESSING, 35);
                if (angelBlessing != null) {
                    entries.add(angelBlessing.when(CtiLootCondition.playerL2Level(400)));
                }

                LootPoolEntryContainer.Builder<?> ichorBottle = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.ICHOR_BOTTLE, 65);
                if (ichorBottle != null) {
                    entries.add(ichorBottle.when(CtiLootCondition.playerL2Level(50)));
                }
                LootPool poolSpellstones = cticonstructLootPool("ctispellstones", -2.0F, 1.0F,
                        entries.toArray(new LootPoolEntryContainer.Builder[0])
                );
                LootTable modified = event.getTable();
                modified.addPool(poolSpellstones);
                event.setTable(modified);
            } else if (SuperpositionHandler.getMergedEnder$EarthenDungeons().contains(event.getName())) {
                List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
                LootPoolEntryContainer.Builder<?> eyeOfNebula = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.EYE_OF_NEBULA, 35);
                if (eyeOfNebula != null) {
                    entries.add(eyeOfNebula.when(CtiLootCondition.playerL2Level(1000)));
                }
                LootPoolEntryContainer.Builder<?> golemHeart = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.GOLEM_HEART, 65);
                if (golemHeart != null) {
                    entries.add(golemHeart.when(CtiLootCondition.playerL2Level(200)));
                }

                LootPool poolSpellstones = cticonstructLootPool("ctispellstones", -2.0F, 1.0F,
                        entries.toArray(new LootPoolEntryContainer.Builder[0])
                );
                LootTable modified = event.getTable();
                modified.addPool(poolSpellstones);
                event.setTable(modified);
            } else if (SuperpositionHandler.getAirDungeons().contains(event.getName())) {
                List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
                LootPoolEntryContainer.Builder<?> angelBlessing = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.ANGEL_BLESSING, 100);
                if (angelBlessing != null) {
                    entries.add(angelBlessing.when(CtiLootCondition.playerL2Level(400)));
                }
                LootPoolEntryContainer.Builder<?> ichorBottle = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.ICHOR_BOTTLE, 50);
                if (ichorBottle != null) {
                    entries.add(ichorBottle.when(CtiLootCondition.playerL2Level(50)));
                }

                LootPool poolSpellstones = cticonstructLootPool("ctispellstones", -2.0F, 1.0F,
                        entries.toArray(new LootPoolEntryContainer.Builder[0])
                );
                LootTable modified = event.getTable();
                modified.addPool(poolSpellstones);
                event.setTable(modified);
            } else if (SuperpositionHandler.getEarthenDungeons().contains(event.getName())) {
                List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
                LootPoolEntryContainer.Builder<?> golemHeart = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.GOLEM_HEART, 100);
                if (golemHeart != null) {
                    entries.add(golemHeart.when(CtiLootCondition.playerL2Level(200)));
                }

                LootPool poolSpellstones = cticonstructLootPool("ctispellstones", -2.0F, 1.0F,
                        entries.toArray(new LootPoolEntryContainer.Builder[0])
                );
                LootTable modified = event.getTable();
                modified.addPool(poolSpellstones);
                event.setTable(modified);
            } else if (SuperpositionHandler.getNetherDungeons().contains(event.getName())) {
                List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
                LootPoolEntryContainer.Builder<?> blazingCore = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.BLAZING_CORE, 100);
                if (blazingCore != null) {
                    entries.add(blazingCore.when(CtiLootCondition.playerL2Level(800)));
                }

                LootPool poolSpellstones = cticonstructLootPool("ctispellstones", -8.0F, 1.0F,
                        entries.toArray(new LootPoolEntryContainer.Builder[0])
                );
                LootTable modified = event.getTable();
                modified.addPool(poolSpellstones);
                event.setTable(modified);
            } else if (SuperpositionHandler.getWaterDungeons().contains(event.getName())) {
                List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
                LootPoolEntryContainer.Builder<?> oceanStone = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.OCEAN_STONE, 500);
                if (oceanStone != null) {
                    entries.add(oceanStone.when(CtiLootCondition.playerL2Level(500)));
                }

                LootPool poolSpellstones = cticonstructLootPool("ctispellstones", -3.0F, 1.0F,
                        entries.toArray(new LootPoolEntryContainer.Builder[0])
                );
                LootTable modified = event.getTable();
                modified.addPool(poolSpellstones);
                event.setTable(modified);
            } else if (SuperpositionHandler.getEnderDungeons().contains(event.getName())) {
                List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
                LootPoolEntryContainer.Builder<?> eyeOfNebula = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.EYE_OF_NEBULA, 90);
                if (eyeOfNebula != null) {
                    entries.add(eyeOfNebula.when(CtiLootCondition.playerL2Level(1000)));
                }
                LootPoolEntryContainer.Builder<?> voidPearl = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.VOID_PEARL, 40);
                if (voidPearl != null) {
                    entries.add(voidPearl.when(CtiLootCondition.playerL2Level(1800)));
                }
                LootPoolEntryContainer.Builder<?> voidPearl2 = SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.VOID_PEARL, 1000);
                if (voidPearl2 != null) {
                    entries.add(voidPearl2.when(CtiLootCondition.playerL2Level(7000)));
                }

                LootPool poolSpellstones = cticonstructLootPool("ctispellstones", -4.0F, 1.0F,
                        entries.toArray(new LootPoolEntryContainer.Builder[0])
                );
                LootTable modified = event.getTable();
                modified.addPool(poolSpellstones);
                event.setTable(modified);
            }
        }
    }

    public static LootPool cticonstructLootPool(String poolName, float minRolls, float maxRolls, @Nullable LootPoolEntryContainer.Builder<?>... entries) {
        LootPool.Builder poolBuilder = LootPool.lootPool();
        poolBuilder.name(poolName);
        poolBuilder.setRolls(UniformGenerator.between(minRolls, maxRolls));
        if (entries != null) {
            for(LootPoolEntryContainer.Builder<?> entry : entries) {
                if (entry != null) {
                    poolBuilder.add(entry);
                }
            }
        }
        return poolBuilder.build();
    }
}
