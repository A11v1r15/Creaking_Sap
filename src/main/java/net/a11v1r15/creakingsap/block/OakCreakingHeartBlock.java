package net.a11v1r15.creakingsap.block;

import net.a11v1r15.creakingsap.block.entity.AcaciaCreakingHeartBlockEntity;
import net.a11v1r15.creakingsap.block.entity.OakCreakingHeartBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;


public class OakCreakingHeartBlock extends AbstractCreakingHeartBlock {
    public OakCreakingHeartBlock(Settings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OakCreakingHeartBlockEntity(pos, state);
    }
    public static boolean shouldBeEnabled(BlockState state, WorldView world, BlockPos pos) {
        Direction.Axis axis = state.get(AXIS);
        Direction[] directions = axis.getDirections();

        for (Direction direction : directions) {
            BlockState blockState = world.getBlockState(pos.offset(direction));
            if (!blockState.isIn(BlockTags.OAK_LOGS) || blockState.get(AXIS) != axis) {
                return false;
            }
        }

        return true;
    }
}
