package net.a11v1r15.creakingsap;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;

public class CreakingSapItems {
    public static final Item CREAKING_SAP_BUCKET = register(
            "creaking_sap_bucket",
            new Item.Settings()
                    .requires(FeatureFlags.WINTER_DROP)
                    .recipeRemainder(Items.BUCKET)
                    .useRemainder(Items.BUCKET)
                    .maxCount(1)
    );

    public static final Item BIRCH_CREAKING_HEART = Items.register(CreakingSapBlocks.BIRCH_CREAKING_HEART);
    public static final Item OAK_CREAKING_HEART = Items.register(CreakingSapBlocks.OAK_CREAKING_HEART);
    public static final Item DARK_OAK_CREAKING_HEART = Items.register(CreakingSapBlocks.DARK_OAK_CREAKING_HEART);
    public static final Item SPRUCE_CREAKING_HEART = Items.register(CreakingSapBlocks.SPRUCE_CREAKING_HEART);
    public static final Item JUNGLE_CREAKING_HEART = Items.register(CreakingSapBlocks.JUNGLE_CREAKING_HEART);

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.addAfter(Items.MILK_BUCKET,
                        CreakingSapItems.CREAKING_SAP_BUCKET));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS)
                .register((itemGroup) -> itemGroup.addBefore(Items.CREAKING_HEART,
                        CreakingSapItems.BIRCH_CREAKING_HEART,
                        CreakingSapItems.OAK_CREAKING_HEART,
                        CreakingSapItems.DARK_OAK_CREAKING_HEART,
                        CreakingSapItems.SPRUCE_CREAKING_HEART,
                        CreakingSapItems.JUNGLE_CREAKING_HEART));
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, CreakingSap.id(id));
    }

    public static Item register(String id, Item.Settings settings) {
        return Items.register(keyOf(id), Item::new, settings);
    }
}
