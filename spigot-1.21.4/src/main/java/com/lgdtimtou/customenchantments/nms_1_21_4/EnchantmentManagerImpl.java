package com.lgdtimtou.customenchantments.nms_1_21_4;


import com.lgdtimtou.customenchantments.enchantments.CustomEnchantDefinition;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantRecord;
import com.lgdtimtou.customenchantments.nms.EnchantmentManager;
import com.lgdtimtou.customenchantments.nms.Reflex;
import com.lgdtimtou.customenchantments.other.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.CraftServer;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftNamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

public class EnchantmentManagerImpl implements EnchantmentManager {

    private static final String REGISTRY_FROZEN_TAGS_FIELD = "j"; // frozenTags
    private static final String REGISTRY_ALL_TAGS_FIELD = "k"; // allTags
    private static final String TAG_SET_UNBOUND_METHOD = "a"; // .unbound()
    private static final String TAG_SET_MAP_FIELD = usingMojangMappings() ? "val$map" : "a";
    private final MappedRegistry<Enchantment> ENCHANTS;
    private final MappedRegistry<Item> ITEMS;
    private final Map<String, TagKey<Enchantment>> enchantmentTags = new HashMap<>();

    public EnchantmentManagerImpl() {
        Util.debug("Using " + (usingMojangMappings() ? "mojang" : "spigot") + "-mappings");
        MinecraftServer SERVER = ((CraftServer) Bukkit.getServer()).getServer();
        ENCHANTS = (MappedRegistry<Enchantment>) SERVER.registryAccess().lookup(Registries.ENCHANTMENT).orElseThrow();
        ITEMS = (MappedRegistry<Item>) SERVER.registryAccess().lookup(Registries.ITEM).orElseThrow();

        fillEnchantmentTags();
    }

    private static boolean usingMojangMappings() {
        try {
            Class.forName("net.minecraft.core.MappedRegistry");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @NotNull
    private <T> ResourceKey<T> getResourceKey(@NotNull Registry<T> registry, @NotNull String name) {
        return ResourceKey.create(registry.key(), ResourceLocation.withDefaultNamespace(name));
    }

    private <T> TagKey<T> getTagKey(@NotNull Registry<T> registry, @NotNull String name) {
        return TagKey.create(registry.key(), ResourceLocation.withDefaultNamespace(name));
    }

    @NotNull
    private <T> Map<TagKey<T>, HolderSet.Named<T>> getFrozenTags(@NotNull MappedRegistry<T> registry) {
        return (Map<TagKey<T>, HolderSet.Named<T>>) Reflex.getFieldValue(registry, REGISTRY_FROZEN_TAGS_FIELD);
    }

    @NotNull
    private <T> Object getAllTags(@NotNull MappedRegistry<T> registry) {
        return Reflex.getFieldValue(registry, REGISTRY_ALL_TAGS_FIELD);
    }

    @NotNull
    private <T> Map<TagKey<T>, HolderSet.Named<T>> getTagsMap(@NotNull Object tagSet) {
        // new HashMap, because original is ImmutableMap.
        return new HashMap<>((Map<TagKey<T>, HolderSet.Named<T>>) Reflex.getFieldValue(tagSet, TAG_SET_MAP_FIELD));
    }

    public void unFreezeRegistry() {
        unfreeze(ENCHANTS);
        unfreeze(ITEMS);
    }

    private <T> void unfreeze(@NotNull MappedRegistry<T> registry) {
        Reflex.setFieldValue(registry, "l", false);             // MappedRegistry#frozen
        Reflex.setFieldValue(registry, "m", new IdentityHashMap<>()); // MappedRegistry#unregisteredIntrusiveHolders
    }

    public void freezeRegistry() {
        freeze(ITEMS);
        freeze(ENCHANTS);
    }

    private <T> void freeze(@NotNull MappedRegistry<T> registry) {
        // Get original TagSet object of the registry before unbound.
        // We MUST keep original TagSet object and only modify an inner map object inside it.
        // Otherwise it will throw an Network Error on client join because of 'broken' tags that were bound to other TagSet object.
        Object tagSet = getAllTags(registry);

        Map<TagKey<T>, HolderSet.Named<T>> tagsMap = getTagsMap(tagSet);
        Map<TagKey<T>, HolderSet.Named<T>> frozenTags = getFrozenTags(registry);

        // Here we add all registered and bound vanilla tags to the 'frozenTags' map for further freeze & bind.
        // For some reason 'frozenTags' map does not contain all the tags, so some of them will be absent if not added back here
        // and result in broken gameplay features.
        tagsMap.forEach(frozenTags::putIfAbsent);

        // We MUST 'unbound' the registry tags to be able to call .freeze() method of it.
        // Otherwise it will throw an error saying tags are not bound.
        unbound(registry);

        // This method will register all tags from the 'frozenTags' map and assign a new TagSet object to the 'allTags' field of registry.
        // But we MUST replace the 'allTags' field value with the original (before unbound) TagSet object to prevent Network Error for clients.
        registry.freeze();

        // Here we need to put in 'tagsMap' map of TagSet object all new/custom registered tags.
        // Otherwise it will cause Network Error because custom tags are not present in the TagSet tags map.
        frozenTags.forEach(tagsMap::putIfAbsent);

        // Update inner tags map of the TagSet object that is 'allTags' field of the registry.
        Reflex.setFieldValue(tagSet, TAG_SET_MAP_FIELD, tagsMap);
        // Assign original TagSet object with modified tags map to the 'allTags' field of the registry.
        Reflex.setFieldValue(registry, REGISTRY_ALL_TAGS_FIELD, tagSet);
    }

    private <T> void unbound(@NotNull MappedRegistry<T> registry) {
        Class<?> tagSetClass = Reflex.getInnerClass(
                MappedRegistry.class.getName(),
                usingMojangMappings() ? "TagSet" : "a"
        );

        Method unboundMethod = Reflex.getMethod(tagSetClass, TAG_SET_UNBOUND_METHOD);
        Object unboundTagSet = Reflex.invokeMethod(unboundMethod, registry); // new TagSet object.

        Reflex.setFieldValue(registry, REGISTRY_ALL_TAGS_FIELD, unboundTagSet);
    }

    public org.bukkit.enchantments.Enchantment registerEnchantment(CustomEnchantRecord customEnchant) {
        String enchantId = customEnchant.getNamespacedName();
        Component component = Component.literal(customEnchant.getName());

        HolderSet<Enchantment> exclusiveSet = createExclusiveSet(customEnchant.getNamespacedName());
        DataComponentMap effects = DataComponentMap.builder().build();

        Set<EnchantmentTarget> enchantmentTargets = customEnchant.getEnchantmentTargets();

        EquipmentSlotGroup[] slots = getSlots(enchantmentTargets);

        Enchantment.EnchantmentDefinition definition = getEnchantmentDefinition(
                customEnchant,
                Util.targetsToMats(enchantmentTargets),
                slots
        );
        Enchantment enchantment = new Enchantment(component, definition, exclusiveSet, effects);

        Holder.Reference<Enchantment> reference = ENCHANTS.createIntrusiveHolder(enchantment);
        Registry.register(ENCHANTS, enchantId, enchantment);

        enchantmentTags.forEach((tagName, tagKey) -> {
            if (customEnchant.getTags().getOrDefault(tagName, false)) {
                addInTag(tagKey, reference);
                if (tagName.equalsIgnoreCase("treasure"))
                    removeFromTag(EnchantmentTags.NON_TREASURE, reference);
            } else {
                removeFromTag(tagKey, reference);
                if (tagName.equalsIgnoreCase("treasure"))
                    addInTag(EnchantmentTags.NON_TREASURE, reference);
            }
        });

        return Util.getEnchantmentByName(customEnchant.getNamespacedName());
    }

    private Enchantment.EnchantmentDefinition getEnchantmentDefinition(CustomEnchantRecord customEnchant, Set<Material> targetItems, EquipmentSlotGroup[] slots) {
        int weight = customEnchant.getEnchantmentTableWeight();
        int maxLevel = customEnchant.getMaxLevel();
        int anvilCost = customEnchant.getAnvilCost();

        HolderSet.Named<Item> supportedItems = createItemSet(
                "enchant_supported",
                customEnchant.getNamespacedName(),
                targetItems
        );
        HolderSet.Named<Item> primaryItems = createItemSet(
                "enchant_primary",
                customEnchant.getNamespacedName(),
                targetItems
        );

        CustomEnchantDefinition.CustomEnchantCost customMinCost = customEnchant.getMinCost();
        CustomEnchantDefinition.CustomEnchantCost customMaxCost = customEnchant.getMaxCost();
        Enchantment.Cost minCost = new Enchantment.Cost(customMinCost.base(), customMinCost.perLevelAboveFirst());
        Enchantment.Cost maxCost = new Enchantment.Cost(customMaxCost.base(), customMaxCost.perLevelAboveFirst());

        return Enchantment.definition(
                supportedItems,
                primaryItems,
                weight,
                maxLevel,
                minCost,
                maxCost,
                anvilCost,
                slots
        );
    }

    @NotNull
    private HolderSet.Named<Item> createItemSet(@NotNull String prefix, @NotNull String enchantId, @NotNull Set<Material> materials) {
        TagKey<Item> customKey = getTagKey(ITEMS, prefix + "/" + enchantId);
        List<Holder<Item>> holders = new ArrayList<>();

        materials.forEach(material -> {
            ResourceLocation location = CraftNamespacedKey.toMinecraft(material.getKey());
            Holder.Reference<Item> holder = ITEMS.get(location).orElse(null);
            if (holder == null) return;

            holders.add(holder);
        });

        ITEMS.bindTag(customKey, holders);

        return getFrozenTags(ITEMS).get(customKey);
    }

    private EquipmentSlotGroup[] getSlots(Set<EnchantmentTarget> targets) {
        for (EnchantmentTarget target : Set.of(
                EnchantmentTarget.ARMOR,
                EnchantmentTarget.ARMOR_FEET,
                EnchantmentTarget.ARMOR_LEGS,
                EnchantmentTarget.ARMOR_TORSO,
                EnchantmentTarget.ARMOR_HEAD,
                EnchantmentTarget.WEARABLE
        ))
            if (targets.contains(target))
                return new EquipmentSlotGroup[]{EquipmentSlotGroup.ARMOR};

        return new EquipmentSlotGroup[]{EquipmentSlotGroup.HAND};
    }


    private void addInTag(@NotNull TagKey<Enchantment> tagKey, @NotNull Holder.Reference<Enchantment> reference) {
        modfiyTag(ENCHANTS, tagKey, reference, List::add);
    }

    private void removeFromTag(@NotNull TagKey<Enchantment> tagKey, @NotNull Holder.Reference<Enchantment> reference) {
        modfiyTag(ENCHANTS, tagKey, reference, List::remove);
    }

    private <T> void modfiyTag(@NotNull MappedRegistry<T> registry, @NotNull TagKey<T> tagKey, @NotNull Holder.Reference<T> reference, @NotNull BiConsumer<List<Holder<T>>, Holder.Reference<T>> consumer) {
        HolderSet.Named<T> holders = registry.get(tagKey).orElse(null);
        if (holders == null) return;

        List<Holder<T>> contents = new ArrayList<>(holders.stream().toList());
        consumer.accept(contents, reference);

        registry.bindTag(tagKey, contents);
    }

    @NotNull
    private HolderSet.Named<Enchantment> createExclusiveSet(@NotNull String enchantId) {
        TagKey<Enchantment> customKey = getTagKey(ENCHANTS, "exclusive_set/" + enchantId);
        List<Holder<Enchantment>> holders = new ArrayList<>();

        ENCHANTS.bindTag(customKey, holders);

        return getFrozenTags(ENCHANTS).get(customKey);
    }

    public void addExclusives(@NotNull String enchantId, @NotNull Set<String> conflicts) {
        ResourceKey<Enchantment> enchantKey = getResourceKey(ENCHANTS, enchantId);
        Enchantment enchantment = ENCHANTS.getValue(enchantKey);
        if (enchantment == null) return;

        TagKey<Enchantment> exclusivesKey = getTagKey(ENCHANTS, "exclusive_set/" + enchantId);

        conflicts.forEach(id -> {
            ResourceKey<Enchantment> conflictKey = getResourceKey(ENCHANTS, id);
            Holder.Reference<Enchantment> reference = ENCHANTS.get(conflictKey).orElse(null);
            if (reference == null) return;

            addInTag(exclusivesKey, reference);
        });
    }

    private void fillEnchantmentTags() {
        Field[] fields = EnchantmentTags.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(TagKey.class)) {
                try {
                    TagKey<Enchantment> tagKey = (TagKey<Enchantment>) field.get(null);
                    ResourceLocation location = tagKey.location();
                    String value = location.toString();
                    if (!value.contains("exclusive") && !value.equals("non_treasure"))
                        enchantmentTags.put(value.replaceAll("/", "_"), tagKey);
                } catch (IllegalAccessException e) {
                    // Ignore
                }
            }
        }
    }
}
