package net.a11v1r15.creakingsap.block;

import com.mojang.serialization.MapCodec;
import net.a11v1r15.creakingsap.block.entity.AbstractCreakingHeartBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CreakingHeartBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class AbstractCreakingHeartBlock extends BlockWithEntity {
    public static final MapCodec<AbstractCreakingHeartBlock> CODEC = createCodec(AbstractCreakingHeartBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS;
    public static final EnumProperty<AbstractCreakingHeartBlock.Creaking> CREAKING;

    public MapCodec<AbstractCreakingHeartBlock> getCodec() {
        return CODEC;
    }

    public AbstractCreakingHeartBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y).with(CREAKING, Creaking.DISABLED));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AbstractCreakingHeartBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        } else {
            return state.get(CREAKING) != Creaking.DISABLED ? validateTicker(type, BlockEntityType.CREAKING_HEART, CreakingHeartBlockEntity::tick) : null;
        }
    }

    public static boolean isWorldNaturalAndNight(World world) {
        return world.getDimension().natural() && world.isNight();
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (isWorldNaturalAndNight(world)) {
            if (state.get(CREAKING) != Creaking.DISABLED) {
                if (random.nextInt(16) == 0 && isSurroundedByLogs(world, pos)) {
                    world.playSound(pos.getX() + world.random.nextBetween(-16, 16), pos.getY() + world.random.nextBetween(-14, 2), pos.getZ() + world.random.nextBetween(-16, 16), SoundEvents.BLOCK_CREAKING_HEART_IDLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                }

            }
        }
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        BlockState blockState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        return enableIfValid(blockState, world, pos);
    }

    private static BlockState enableIfValid(BlockState state, WorldView world, BlockPos pos) {
        boolean bl = shouldBeEnabled(state, world, pos);
        Creaking creaking = state.get(CREAKING);
        return bl && creaking == Creaking.DISABLED ? state.with(CREAKING, Creaking.DORMANT) : state;
    }

    public static boolean shouldBeEnabled(BlockState state, WorldView world, BlockPos pos) {
        Direction.Axis axis = state.get(AXIS);
        Direction[] directions = axis.getDirections();

        for (Direction direction : directions) {
            BlockState blockState = world.getBlockState(pos.offset(direction));
            if (!blockState.isIn(BlockTags.BIRCH_LOGS) || blockState.get(AXIS) != axis) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSurroundedByLogs(WorldAccess world, BlockPos pos) {
        Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            if (!blockState.isIn(BlockTags.LOGS)) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return enableIfValid(this.getDefaultState().with(AXIS, ctx.getSide().getAxis()), ctx.getWorld(), ctx.getBlockPos());
    }

    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return PillarBlock.changeRotation(state, rotation);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, CREAKING);
    }

    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        BlockEntity var7 = world.getBlockEntity(pos);
        if (var7 instanceof AbstractCreakingHeartBlockEntity abstractCreakingHeartBlockEntity) {
            abstractCreakingHeartBlockEntity.onBreak(null);
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity var6 = world.getBlockEntity(pos);
        if (var6 instanceof AbstractCreakingHeartBlockEntity abstractCreakingHeartBlockEntity) {
            abstractCreakingHeartBlockEntity.onBreak(player.getDamageSources().playerAttack(player));
        }

        return super.onBreak(world, pos, state, player);
    }

    static {
        AXIS = Properties.AXIS;
        CREAKING = EnumProperty.of("creaking", Creaking.class);
    }

    public enum Creaking implements StringIdentifiable {
        DISABLED("disabled"),
        DORMANT("dormant"),
        ACTIVE("active");

        private final String name;

        Creaking(final String name) {
            this.name = name;
        }

        public String asString() {
            return this.name;
        }
    }
}
