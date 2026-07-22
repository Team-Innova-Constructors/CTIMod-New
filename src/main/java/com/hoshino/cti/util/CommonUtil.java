package com.hoshino.cti.util;

import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.google.common.collect.Lists;
import com.hoshino.cti.Modifier.Armor.AntiCurse;
import com.hoshino.cti.mixin.LivingEntityAccessor;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.DummyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.ArmorSlotType;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonUtil {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String TAG_MACHINE_PARALLEL = "MachineParallel";
    public static final String MISSING_MATERIAL_KEY = TConstruct.makeTranslationKey("tooltip", "part.missing_material");
    public static final String MISSING_STATS_KEY = TConstruct.makeTranslationKey("tooltip", "part.missing_stats");

    public static void Reload(MinecraftServer server) {
        LOGGER.info("Mod cti is now conducting a reload! It will take 114514 years.");
        PackRepository repository = server.getPackRepository();
        WorldData data = server.getWorldData();
        Collection<String> collection1 = repository.getSelectedIds();
        Collection<String> collection2 = discoverNewPacks(repository, data, collection1);
        reloadPacks(collection2, server);
    }

    public static void reloadPacks(Collection<String> p_138236_, MinecraftServer server) {
        server.reloadResources(p_138236_).exceptionally((p_138234_) -> {
            LOGGER.warn("Failed to execute reload", p_138234_);
            return null;
        });
    }

    public static Collection<String> discoverNewPacks(PackRepository repository, WorldData data, Collection<String> collection) {
        repository.reload();
        Collection<String> $$3 = Lists.newArrayList(collection);
        Collection<String> $$4 = data.getDataPackConfig().getDisabled();
        Iterator var5 = repository.getAvailableIds().iterator();

        while (var5.hasNext()) {
            String $$5 = (String) var5.next();
            if (!$$4.contains($$5) && !$$3.contains($$5)) {
                $$3.add($$5);
            }
        }

        return $$3;
    }

    public static UUID UUIDFromSlot(EquipmentSlot slot, ModifierId modifierId){
        return UUID.nameUUIDFromBytes((slot.getName() +modifierId.toString()).getBytes(StandardCharsets.UTF_8));
    }
    public static UUID UUIDFromSlotAndAttribute(EquipmentSlot slot, ModifierId modifierId, Attribute attribute){
        return UUID.nameUUIDFromBytes((slot.getName() +modifierId.toString()+attribute.getDescriptionId()).getBytes(StandardCharsets.UTF_8));
    }
    public static UUID UUIDFromAnyString(String s){
        return UUID.nameUUIDFromBytes(s.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isAetherNight(Level level){
        if (level.dimension()== AetherDimensions.AETHER_LEVEL){
            return level.getDayTime()%72000>39000&&level.getDayTime()%72000<69000;
        }
        return false;
    }

    public static int getArmorLevelingValue(TinkerDataCapability.TinkerDataKey<Integer> key, LivingEntity living){
        AtomicInteger atomicInteger = new AtomicInteger(0);
        living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->atomicInteger.set(cap.get(key,0)));
        return atomicInteger.get();
    }
    public static boolean hasArmorLevel(TinkerDataCapability.TinkerDataKey<Integer> key,LivingEntity living){
        return getArmorLevelingValue(key,living)>0;
    }

    public static int getAntiCurseLevel(LivingEntity living){
        return getArmorLevelingValue(AntiCurse.KEY_ANTI_CURSE,living);
    }

    public static final ResourceLocation SLOT_SEAL = new ResourceLocation("thermal","slot_seal");

    public static Item itemFromId(String id){
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
    }

    public static List<ModifierEntry> getModifiersFromPart(ItemStack stack){
        if (stack.getItem() instanceof IToolPart part){
            var statType = part.getStatType();
            var material = part.getMaterial(stack).getId();
            return MaterialRegistry.getInstance().getTraits(material,statType);
        }
        return List.of();
    }

    public static float getPlayerAttackDelay(Player player){
        float delay = player.getCurrentItemAttackStrengthDelay();
        if (delay < 10) {
            return delay * 0.7f + 3f;
        }
        return delay;
    }
    public static List<SlotType> getResultSlotTypes(ModifierEntry result) {
        ModDataNBT persistentData = new ModDataNBT();
        ModDataNBT volatileData = new ModDataNBT();
        result.getHook(ModifierHooks.VOLATILE_DATA).addVolatileData(new DummyToolStack(TinkerTools.sword.get(), ModifierNBT.EMPTY, persistentData), result, volatileData);
        result.getHook(ModifierHooks.VOLATILE_DATA).addVolatileData(new DummyToolStack(TinkerTools.plateArmor.get(ArmorSlotType.CHESTPLATE), ModifierNBT.EMPTY, persistentData), result, volatileData);
        ArrayList<SlotType> list = new ArrayList<>();
        SlotType.getAllSlotTypes().forEach(type -> {
            int count = volatileData.getSlots(type);
            if (count > 0) {
                list.add(type);
            }
        });
        return list;
    }

    public static int applyOreFortuneBonus(RandomSource pRandom, int pOriginalCount, int pEnchantmentLevel) {
        if (pEnchantmentLevel > 0) {
            int i = pRandom.nextInt(pEnchantmentLevel + 2) - 1;
            if (i < 0) {
                i = 0;
            }
            return pOriginalCount * (i + 1);
        } else {
            return pOriginalCount;
        }
    }

}
