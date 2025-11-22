package com.hoshino.cti.register;

import com.hoshino.cti.Capabilitiess.PlayerEnigmaticItemsCondition;
import com.hoshino.cti.Cti;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CtiLootCondition {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES =
            DeferredRegister.create(Registry.LOOT_CONDITION_TYPE.key(), Cti.MOD_ID);


    public static final RegistryObject<LootItemConditionType> PLAYER_LEVEL_CONDITION =
            LOOT_CONDITION_TYPES.register("player_level",
                    () -> new LootItemConditionType(new PlayerEnigmaticItemsCondition.PlayerLevelConditionSerializer()));


    public static LootItemCondition.Builder playerL2Level(int level) {
        return PlayerEnigmaticItemsCondition.Builder.builder(level);
    }
}
