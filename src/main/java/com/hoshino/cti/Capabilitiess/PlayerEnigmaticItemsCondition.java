package com.hoshino.cti.Capabilitiess;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.hoshino.cti.register.CtiLootCondition;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public class PlayerEnigmaticItemsCondition implements LootItemCondition {
    private final int requiredLevel;

    public PlayerEnigmaticItemsCondition(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }
    @Override
    public boolean test(LootContext context) {
        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            Entity entity = context.getParam(LootContextParams.THIS_ENTITY);
            if (entity instanceof Player player) {
                if(SuperpositionHandler.isTheCursedOne(player)){
                    int l2Level= DifficultyLevel.ofAny(player);
                    return l2Level > 300;
                } else return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull LootItemConditionType getType() {
        return CtiLootCondition.PLAYER_LEVEL_CONDITION.get();
    }
    public static class Builder implements LootItemCondition.Builder {
        private final int requiredLevel;

        private Builder(int requiredLevel) {
            this.requiredLevel = requiredLevel;
        }

        public static Builder builder(int level) {
            return new Builder(level);
        }

        @Override
        public PlayerEnigmaticItemsCondition build() {
            return new PlayerEnigmaticItemsCondition(requiredLevel);
        }
    }
    public static class PlayerLevelConditionSerializer implements Serializer<PlayerEnigmaticItemsCondition> {
        @Override
        public void serialize(JsonObject jsonObject, PlayerEnigmaticItemsCondition playerEnigmaticItemsCondition, JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("min_level", playerEnigmaticItemsCondition.requiredLevel);
        }

        @Override
        public PlayerEnigmaticItemsCondition deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            int level = GsonHelper.getAsInt(jsonObject, "min_level");
            return new PlayerEnigmaticItemsCondition(level);
        }
    }
}
