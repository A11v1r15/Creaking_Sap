package net.a11v1r15.creakingsap;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class CreakingSapItems {
    public static final Item CREAKING_SAP_BUCKET = register(
            "creaking_sap_bucket",
            new Item.Settings()
                    .requires(FeatureFlags.WINTER_DROP)
                    .recipeRemainder(Items.BUCKET)
                    .useRemainder(Items.BUCKET)
                    .maxCount(1)
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
            .register((itemGroup) -> itemGroup.addAfter(Items.MILK_BUCKET, CreakingSapItems.CREAKING_SAP_BUCKET));
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, CreakingSap.id(id));
    }

    public static Item register(String id, Item.Settings settings) {
        return Items.register(keyOf(id), Item::new, settings);
    }
}
