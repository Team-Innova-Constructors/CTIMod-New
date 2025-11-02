package com.hoshino.cti.util;

import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

public class EquipmentUtil {
    public static final List<EquipmentSlot> ARMOR = List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);
    public static final List<EquipmentSlot> HAND = List.of(EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND);
    public static final List<EquipmentSlot> ALL = List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND);
}
