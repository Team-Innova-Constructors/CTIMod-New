package com.hoshino.cti.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;

import java.util.UUID;

public class SkullHelper {
    public static UUID getPlayerUUIDFromHead(ItemStack playerHeadStack) {
        if (playerHeadStack == null || !playerHeadStack.hasTag()) {
            return null;
        }
        CompoundTag rootTag = playerHeadStack.getTag();
        if (rootTag == null) {
            return null;
        }
        if (rootTag.contains(PlayerHeadItem.TAG_SKULL_OWNER, CompoundTag.TAG_COMPOUND)) {
            CompoundTag skullOwnerTag = rootTag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER);
            GameProfile gameProfile = NbtUtils.readGameProfile(skullOwnerTag);
            if (gameProfile != null && gameProfile.getId() != null) {
                return gameProfile.getId();
            }
        }
        else if (rootTag.contains(PlayerHeadItem.TAG_SKULL_OWNER, CompoundTag.TAG_STRING)) {
            return null;
        }
        return null;
    }
}