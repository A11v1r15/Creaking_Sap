package net.a11v1r15.creakingsap.block;

import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;


public class CrimsonCreakingHeartBlock extends AbstractCreakingHeartBlock {
    public CrimsonCreakingHeartBlock(Settings settings) {
        super(settings);
    }

    public static boolean shouldBeEnabled(BlockState state, WorldView world, BlockPos pos) {
        Direction.Axis axis = state.get(AXIS);
        Direction[] directions = axis.getDirections();

        for (Direction direction : directions) {
            BlockState blockState = world.getBlockState(pos.offset(direction));
            if (!blockState.isIn(BlockTags.CRIMSON_STEMS) || blockState.get(AXIS) != axis) {
                return false;
            }
        }

        return true;
    }
}
