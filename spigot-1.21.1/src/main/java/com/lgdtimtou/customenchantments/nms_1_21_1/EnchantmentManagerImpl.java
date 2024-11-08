package com.lgdtimtou.customenchantments.nms_1_21_1;

import com.lgdtimtou.customenchantments.enchantments.CustomEnchantDefinition;
import com.lgdtimtou.customenchantments.enchantments.CustomEnchantRecord;
import com.lgdtimtou.customenchantments.nms.EnchantmentManager;
import com.lgdtimtou.customenchantments.nms.Reflex;
import com.lgdtimtou.customenchantments.other.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
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
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftNamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

public class EnchantmentManagerImpl implements EnchantmentManager {

    private final Map<String, TagKey<Enchantment>> enchantmentTags = new HashMap<>();

    private final MinecraftServer SERVER;
    private final Registry<Enchantment> ENCHANTMENT_REGISTRY;

    private static final String HOLDER_SET_NAMED_CONTENTS_FIELD = "c"; // 'contents' field of the HolderSet.Named
    private static final String HOLDER_REFERENCE_TAGS_FIELD = "b"; // 'tags' field of the Holder.Reference
    private static final String HOLDER_SET_DIRECT_CONTENTS_FIELD = "b";


    public EnchantmentManagerImpl() {
        SERVER = ((CraftServer) Bukkit.getServer()).getServer();
        ENCHANTMENT_REGISTRY = SERVER.registryAccess().registry(Registries.ENCHANTMENT).orElse(null);

        fillEnchantmentTags();
    }

    public void unFreezeRegistry() {
        Reflex.setFieldValue(ENCHANTMENT_REGISTRY, "l", false);             // MappedRegistry#frozen
        Reflex.setFieldValue(ENCHANTMENT_REGISTRY, "m", new IdentityHashMap<>());
    }

    public void freezeRegistry() {
        ENCHANTMENT_REGISTRY.freeze();
    }

    public org.bukkit.enchantments.Enchantment registerEnchantment(CustomEnchantRecord customEnchant) {
        String enchantId = customEnchant.getNamespacedName();
        Component component = Component.literal(customEnchant.getName());

        HolderSet<Enchantment> exclusiveSet = HolderSet.direct();
        DataComponentMap effects = DataComponentMap.builder().build();

        Set<EnchantmentTarget> enchantmentTargets = customEnchant.getEnchantmentTargets();

        EquipmentSlotGroup[] slots = getSlots(enchantmentTargets);

        Enchantment.EnchantmentDefinition definition = getEnchantmentDefinition(customEnchant, Util.targetsToMats(enchantmentTargets), slots);
        Enchantment enchantment = new Enchantment(component, definition, exclusiveSet, effects);

        Holder.Reference<Enchantment> reference = ENCHANTMENT_REGISTRY.createIntrusiveHolder(enchantment);
        Registry.register(ENCHANTMENT_REGISTRY, enchantId, enchantment);

        enchantmentTags.forEach((tagName, tagKey) -> {
            if (customEnchant.getTags().getOrDefault(tagName, false)) {
                addInTag(tagKey, reference);
                if (tagName.equalsIgnoreCase("treasure"))
                    removeFromTag(EnchantmentTags.NON_TREASURE, reference);
            }
            else {
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

        HolderSet.Named<Item> supportedItems = createItemSet("enchant_supported", customEnchant.getNamespacedName(), targetItems);
        HolderSet.Named<Item> primaryItems = createItemSet("enchant_primary", customEnchant.getNamespacedName(), targetItems);

        CustomEnchantDefinition.CustomEnchantCost customMinCost = customEnchant.getMinCost();
        CustomEnchantDefinition.CustomEnchantCost customMaxCost = customEnchant.getMaxCost();
        Enchantment.Cost minCost = new Enchantment.Cost(customMinCost.base(), customMinCost.perLevelAboveFirst());
        Enchantment.Cost maxCost = new Enchantment.Cost(customMaxCost.base(), customMaxCost.perLevelAboveFirst());

        return Enchantment.definition(supportedItems, primaryItems, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    private ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace(Util.getNamedspacedName(name)));
    }

    @SuppressWarnings("unchecked")
    private HolderSet.Named<Item> createItemSet(String prefix, String enchantId, Set<Material> materials) {
        Registry<Item> items = SERVER.registryAccess().registry(Registries.ITEM).orElseThrow();
        TagKey<Item> customKey = TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(prefix + "/" + enchantId));
        HolderSet.Named<Item> customItems = items.getOrCreateTag(customKey);
        List<Holder<Item>> holders = new ArrayList<>();

        materials.forEach(material -> {
            ResourceLocation location = CraftNamespacedKey.toMinecraft(material.getKey());
            Holder.Reference<Item> holder = items.getHolder(location).orElse(null);
            if (holder == null) return;

            // We must reassign the 'tags' field value because of the HolderSet#contains(Holder<T> holder) behavior.
            // It checks if Holder.Reference.is(this.key) -> Holder.Reference.tags.contains(key). Where 'key' is our custom key created above.
            // So, even if our HolderSet content is filled with items, we have to include their tag to the actual items in registry.
            Set<TagKey<Item>> holderTags = new HashSet<>((Set<TagKey<Item>>) Reflex.getFieldValue(holder, HOLDER_REFERENCE_TAGS_FIELD));
            holderTags.add(customKey);
            Reflex.setFieldValue(holder, HOLDER_REFERENCE_TAGS_FIELD, holderTags);

            holders.add(holder);
        });

        Reflex.setFieldValue(customItems, HOLDER_SET_NAMED_CONTENTS_FIELD, holders);

        return customItems;
    }

    private EquipmentSlotGroup[] getSlots(Set<EnchantmentTarget> targets) {
        for (EnchantmentTarget target : Set.of(EnchantmentTarget.ARMOR, EnchantmentTarget.ARMOR_FEET, EnchantmentTarget.ARMOR_LEGS, EnchantmentTarget.ARMOR_TORSO, EnchantmentTarget.ARMOR_HEAD, EnchantmentTarget.WEARABLE))
            if (targets.contains(target))
                return new EquipmentSlotGroup[] {EquipmentSlotGroup.ARMOR};

        return new EquipmentSlotGroup[] {EquipmentSlotGroup.HAND};
    }


    private void addInTag(TagKey<Enchantment> tagKey, Holder.Reference<Enchantment> reference) {
        modfiyTag(tagKey, reference, List::add);
    }

    private void removeFromTag(TagKey<Enchantment> tagKey, Holder.Reference<Enchantment> reference) {
        modfiyTag(tagKey, reference, List::remove);
    }

    private void modfiyTag(
            TagKey<Enchantment> tagKey,
            Holder.Reference<Enchantment> reference,
            BiConsumer<List<Holder<Enchantment>>, Holder.Reference<Enchantment>> consumer
    ) {
        // Get HolderSet of the TagKey
        HolderSet.Named<Enchantment> holders = ENCHANTMENT_REGISTRY.getTag(tagKey).orElse(null);
        if (holders == null) {
            Bukkit.getLogger().warning(tagKey + ": Could not modify HolderSet. HolderSet is NULL.");
            return;
        }

        modfiyHolderSetContents(holders, reference, consumer);
    }

    @SuppressWarnings("unchecked")
    private <T> void modfiyHolderSetContents(
            HolderSet.Named<T> holders,
            Holder.Reference<T> reference,
            BiConsumer<List<Holder<T>>, Holder.Reference<T>> consumer
    ) {
        // We must use reflection to get a mutable Holder list from the HolderSet.
        List<Holder<T>> contents = new ArrayList<>((List<Holder<T>>) Reflex.getFieldValue(holders, HOLDER_SET_NAMED_CONTENTS_FIELD));
        // Do something with it.
        consumer.accept(contents, reference);
        // Assign it back to the HolderSet.
        Reflex.setFieldValue(holders, HOLDER_SET_NAMED_CONTENTS_FIELD, contents);
    }


    public void addExclusives(String enchantId, Set<String> exclusives) {
        Enchantment enchantment = ENCHANTMENT_REGISTRY.get(key(enchantId));
        if (enchantment == null) {
            Bukkit.getLogger().warning(enchantId + ": Could not set exclusive item list. Enchantment is not registered.");
            return;
        }

        HolderSet<Enchantment> exclusiveSet = enchantment.exclusiveSet();
        List<Holder<Enchantment>> contents = new ArrayList<>();

        exclusives.forEach(id -> {
            ResourceKey<Enchantment> key = key(id);
            Holder.Reference<Enchantment> reference = ENCHANTMENT_REGISTRY.getHolder(key).orElse(null);
            if (reference == null) return;

            contents.add(reference);
        });

        Reflex.setFieldValue(exclusiveSet, HOLDER_SET_DIRECT_CONTENTS_FIELD, contents);
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
                } catch(IllegalAccessException e){
                    // Ignore
                }
            }
        }
    }




}
