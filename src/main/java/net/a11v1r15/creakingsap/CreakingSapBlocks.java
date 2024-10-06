package net.a11v1r15.creakingsap;

import net.a11v1r15.creakingsap.block.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class CreakingSapBlocks {
    private static AbstractBlock.Settings CreakingHearthSettings =
            AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(5.0F)
                    .sounds(BlockSoundGroup.CREAKING_HEART)
                    .requires(FeatureFlags.WINTER_DROP);

    public static final Block OAK_CREAKING_HEART = register(
            "oak_creaking_heart",
            OakCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block SPRUCE_CREAKING_HEART = register(
            "spruce_creaking_heart",
            AbstractCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block BIRCH_CREAKING_HEART = register(
            "birch_creaking_heart",
            BirchCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block JUNGLE_CREAKING_HEART = register(
            "jungle_creaking_heart",
            JungleCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block ACACIA_CREAKING_HEART = register(
            "acacia_creaking_heart",
            AcaciaCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block DARK_OAK_CREAKING_HEART = register(
            "dark_oak_creaking_heart",
            DarkOakCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block MANGROVE_CREAKING_HEART = register(
            "mangrove_creaking_heart",
            MangroveCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block CHERRY_CREAKING_HEART = register(
            "cherry_creaking_heart",
            CherryCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block CRIMSON_CREAKING_HEART = register(
            "crimson_creaking_heart",
            CrimsonCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block WARPED_CREAKING_HEART = register(
            "warped_creaking_heart",
            WarpedCreakingHeartBlock::new,
            CreakingHearthSettings
    );
    public static final Block BAMBOO_CREAKING_HEART = register(
            "bamboo_creaking_heart",
            BambooCreakingHeartBlock::new,
            CreakingHearthSettings
    );

    public static void initialize() {
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, CreakingSap.id(id));
    }

    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return Blocks.register(keyOf(id), factory, settings);
    }
}
