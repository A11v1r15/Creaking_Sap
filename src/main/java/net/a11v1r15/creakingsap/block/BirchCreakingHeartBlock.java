package net.a11v1r15.creakingsap.block;

import net.a11v1r15.creakingsap.block.entity.BirchCreakingHeartBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;


public class BirchCreakingHeartBlock extends CreakingHeartBlock {
    public BirchCreakingHeartBlock(Settings settings) {
        super(settings);
    }

    private static TagKey<Block> getWood() {
        return BlockTags.BIRCH_LOGS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BirchCreakingHeartBlockEntity(pos, state);
    }
}
