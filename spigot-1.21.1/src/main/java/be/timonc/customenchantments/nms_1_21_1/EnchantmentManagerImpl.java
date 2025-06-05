package be.timonc.customenchantments.nms_1_21_1;

import be.timonc.customenchantments.enchantments.CustomEnchant;
import be.timonc.customenchantments.enchantments.CustomEnchantDefinition;
import be.timonc.customenchantments.enchantments.CustomEnchantRecord;
import be.timonc.customenchantments.nms.EnchantmentManager;
import be.timonc.customenchantments.nms.Reflex;
import be.timonc.customenchantments.other.Util;
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
import net.minecraft.world.level.block.Block;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

public class EnchantmentManagerImpl implements EnchantmentManager {

    private static final String HOLDER_SET_NAMED_CONTENTS_FIELD = "c"; // 'contents' field of the HolderSet.Named
    private static final String HOLDER_REFERENCE_TAGS_FIELD = "b"; // 'tags' field of the Holder.Reference
    private static final String HOLDER_SET_DIRECT_CONTENTS_FIELD = "b";
    private final Map<String, TagKey<Enchantment>> enchantmentTags = new HashMap<>();
    private final MinecraftServer SERVER;
    private final MappedRegistry<Enchantment> ENCHANTS;
    private final MappedRegistry<Item> ITEMS;
    private final MappedRegistry<Block> BLOCKS;


    public EnchantmentManagerImpl() {
        SERVER = ((CraftServer) Bukkit.getServer()).getServer();
        ENCHANTS = (MappedRegistry<Enchantment>) SERVER.registryAccess().registry(Registries.ENCHANTMENT).orElseThrow();
        ITEMS = (MappedRegistry<Item>) SERVER.registryAccess().registry(Registries.ITEM).orElseThrow();
        BLOCKS = (MappedRegistry<Block>) SERVER.registryAccess().registry(Registries.BLOCK).orElseThrow();

        fillEnchantmentTags();
    }

    @NotNull
    private <T> ResourceKey<T> getResourceKey(@NotNull Registry<T> registry, @NotNull String name) {
        return ResourceKey.create(registry.key(), ResourceLocation.withDefaultNamespace(name));
    }

    private <T> TagKey<T> getTagKey(@NotNull Registry<T> registry, @NotNull String name) {
        return TagKey.create(registry.key(), ResourceLocation.withDefaultNamespace(name));
    }

    public void unFreezeRegistry() {
        Reflex.setFieldValue(ENCHANTS, "l", false);             // MappedRegistry#frozen
        Reflex.setFieldValue(ENCHANTS, "m", new IdentityHashMap<>());
    }

    public void freezeRegistry() {
        ENCHANTS.freeze();
    }

    public org.bukkit.enchantments.Enchantment registerEnchantment(CustomEnchantRecord customEnchant) {
        String enchantId = customEnchant.getNamespacedName();
        Component component = Component.literal(customEnchant.getName());

        HolderSet<Enchantment> exclusiveSet = HolderSet.direct();
        DataComponentMap effects = DataComponentMap.builder().build();

        Enchantment.EnchantmentDefinition definition = getEnchantmentDefinition(customEnchant);
        Enchantment enchantment = new Enchantment(component, definition, exclusiveSet, effects);
        ENCHANTS.createIntrusiveHolder(enchantment);
        Registry.register(ENCHANTS, enchantId, enchantment);

        return Util.getEnchantmentByName(customEnchant.getNamespacedName());
    }

    private Enchantment.EnchantmentDefinition getEnchantmentDefinition(CustomEnchantRecord customEnchant) {
        int weight = customEnchant.getEnchantmentTableWeight();
        int maxLevel = customEnchant.getMaxLevel();
        int anvilCost = customEnchant.getAnvilCost();

        HolderSet.Named<Item> supportedItems = createItemSet(
                "enchant_supported",
                customEnchant.getNamespacedName(),
                customEnchant.getSupportedItems(),
                false
        );
        HolderSet.Named<Item> primaryItems = createItemSet(
                "enchant_primary",
                customEnchant.getNamespacedName(),
                customEnchant.getPrimaryItems(),
                true
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
                EquipmentSlotGroup.ANY
        );
    }

    private ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(
                Registries.ENCHANTMENT,
                ResourceLocation.withDefaultNamespace(Util.getNamedspacedName(name))
        );
    }

    @NotNull
    private HolderSet.Named<Item> createItemSet(@NotNull String prefix, @NotNull String enchantId, @NotNull Set<String> tagKeys, boolean enchantingTable) {
        TagKey<Item> customKey = getTagKey(ITEMS, prefix + "/" + enchantId);

        HolderSet.Named<Item> customItems = ITEMS.getOrCreateTag(customKey);
        List<Holder<Item>> holders = new ArrayList<>();

        for (String tagKeyString : tagKeys) {
            TagKey<Item> itemTagKey = getTagKey(ITEMS, tagKeyString);
            Iterable<Holder<Item>> itemHolders = ITEMS.getTagOrEmpty(itemTagKey);

            if (!itemHolders.iterator().hasNext()) {
                ResourceLocation location = getTagKey(BLOCKS, tagKeyString).location();
                Holder.Reference<Item> holder = ITEMS.getHolder(location).orElse(null);
                if (holder == null) continue;
                updateAndAddHolder(holders, holder, customKey);
            } else {
                itemHolders.forEach(itemHolder -> updateAndAddHolder(holders, itemHolder, customKey));
            }
        }

        Reflex.setFieldValue(customItems, HOLDER_SET_NAMED_CONTENTS_FIELD, holders);
        return customItems;
    }

    private void updateAndAddHolder(List<Holder<Item>> holders, Holder<Item> itemHolder, TagKey<Item> customKey) {
        // We must reassign the 'tags' field value because of the HolderSet#contains(Holder<T> holder) behavior.
        // It checks if Holder.Reference.is(this.key) -> Holder.Reference.tags.contains(key). Where 'key' is our custom key created above.
        // So, even if our HolderSet content is filled with items, we have to include their tag to the actual items in registry.
        Set<TagKey<Item>> holderTags = new HashSet<>((Set<TagKey<Item>>) Reflex.getFieldValue(
                itemHolder,
                HOLDER_REFERENCE_TAGS_FIELD
        ));
        holderTags.add(customKey);
        Reflex.setFieldValue(itemHolder, HOLDER_REFERENCE_TAGS_FIELD, holderTags);
        holders.add(itemHolder);
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
        HolderSet.Named<Enchantment> holders = ENCHANTS.getTag(tagKey).orElse(null);
        if (holders == null) return;

        modfiyHolderSetContents(holders, reference, consumer);
    }

    @SuppressWarnings("unchecked")
    private <T> void modfiyHolderSetContents(
            HolderSet.Named<T> holders,
            Holder.Reference<T> reference,
            BiConsumer<List<Holder<T>>, Holder.Reference<T>> consumer
    ) {
        // We must use reflection to get a mutable Holder list from the HolderSet.
        List<Holder<T>> contents = new ArrayList<>((List<Holder<T>>) Reflex.getFieldValue(
                holders,
                HOLDER_SET_NAMED_CONTENTS_FIELD
        ));
        // Do something with it.
        consumer.accept(contents, reference);
        // Assign it back to the HolderSet.
        Reflex.setFieldValue(holders, HOLDER_SET_NAMED_CONTENTS_FIELD, contents);
    }

    public void addExclusives(String enchantId, Set<String> exclusives) {
        Enchantment enchantment = ENCHANTS.get(key(enchantId));
        if (enchantment == null) {
            Bukkit.getLogger()
                  .warning(enchantId + ": Could not set exclusive item list. Enchantment is not registered.");
            return;
        }

        HolderSet<Enchantment> exclusiveSet = enchantment.exclusiveSet();
        List<Holder<Enchantment>> contents = new ArrayList<>();

        exclusives.forEach(id -> {
            ResourceKey<Enchantment> key = key(id);
            Holder.Reference<Enchantment> reference = ENCHANTS.getHolder(key).orElse(null);
            if (reference == null) return;

            contents.add(reference);
        });

        Reflex.setFieldValue(exclusiveSet, HOLDER_SET_DIRECT_CONTENTS_FIELD, contents);
    }

    @Override
    public void addTagsOnReload(@NotNull CustomEnchant customEnchant) {
        ResourceLocation location = ResourceLocation.withDefaultNamespace(customEnchant.getNamespacedName());
        Holder.Reference<Enchantment> reference = ENCHANTS.getHolder(location).orElse(null);
        if (reference == null) {
            Util.error("Custom Enchantment " + customEnchant.getNamespacedName() + " was not found in enchantment registry! Could not add tags");
            return;
        }
        addTags(reference, customEnchant.getTags());
    }

    private void addTags(Holder.Reference<Enchantment> reference, Map<String, Boolean> tags) {
        enchantmentTags.forEach((tagName, tagKey) -> {
            if (tags.getOrDefault(tagName, false)) {
                addInTag(tagKey, reference);
                if (tagName.equalsIgnoreCase("treasure"))
                    removeFromTag(EnchantmentTags.NON_TREASURE, reference);
            } else {
                removeFromTag(tagKey, reference);
                if (tagName.equalsIgnoreCase("treasure"))
                    addInTag(EnchantmentTags.NON_TREASURE, reference);
            }
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
