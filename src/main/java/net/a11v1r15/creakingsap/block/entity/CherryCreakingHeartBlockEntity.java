package net.a11v1r15.creakingsap.block.entity;

import net.a11v1r15.creakingsap.block.AbstractCreakingHeartBlock;
import net.a11v1r15.creakingsap.block.CherryCreakingHeartBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class CherryCreakingHeartBlockEntity extends AbstractCreakingHeartBlockEntity {
    public CherryCreakingHeartBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, AbstractCreakingHeartBlockEntity blockEntity) {
        if (blockEntity.trailParticlesSpawnTimer > 0) {
            if (blockEntity.trailParticlesSpawnTimer > 50) {
                blockEntity.spawnTrailParticles((ServerWorld)world, 1, true);
                blockEntity.spawnTrailParticles((ServerWorld)world, 1, false);
            }

            if (blockEntity.trailParticlesSpawnTimer % 10 == 0 && world instanceof ServerWorld serverWorld) {
                if (blockEntity.lastCreakingPuppetPos != null) {
                    if (blockEntity.creakingPuppet != null) {
                        blockEntity.lastCreakingPuppetPos = blockEntity.creakingPuppet.getBoundingBox().getCenter();
                    }

                    Vec3d vec3d = Vec3d.ofCenter(pos);
                    float f = 0.2F + 0.8F * (float)(100 - blockEntity.trailParticlesSpawnTimer) / 100.0F;
                    Vec3d vec3d2 = vec3d.subtract(blockEntity.lastCreakingPuppetPos).multiply(f).add(blockEntity.lastCreakingPuppetPos);
                    BlockPos blockPos = BlockPos.ofFloored(vec3d2);
                    float g = (float)blockEntity.trailParticlesSpawnTimer / 2.0F / 100.0F + 0.5F;
                    serverWorld.playSound(null, blockPos, SoundEvents.BLOCK_CREAKING_HEART_HURT, SoundCategory.BLOCKS, g, 1.0F);
                }
            }

            --blockEntity.trailParticlesSpawnTimer;
        }

        if (blockEntity.creakingUpdateTimer-- < 0) {
            blockEntity.creakingUpdateTimer = 20;
            if (blockEntity.creakingPuppet != null) {
                if (CherryCreakingHeartBlock.isWorldNaturalAndNight(world) && !(blockEntity.creakingPuppet.squaredDistanceTo(Vec3d.ofBottomCenter(pos)) > 1156.0)) {
                    if (blockEntity.creakingPuppet.isRemoved()) {
                        blockEntity.creakingPuppet = null;
                    }

                    if (!CherryCreakingHeartBlock.shouldBeEnabled(state, world, pos) && blockEntity.creakingPuppet == null) {
                        world.setBlockState(pos, state.with(AbstractCreakingHeartBlock.CREAKING, AbstractCreakingHeartBlock.Creaking.DISABLED), 3);
                    }

                } else {
                    blockEntity.onBreak(null);
                }
            } else if (!CherryCreakingHeartBlock.shouldBeEnabled(state, world, pos)) {
                world.setBlockState(pos, state.with(AbstractCreakingHeartBlock.CREAKING, AbstractCreakingHeartBlock.Creaking.DISABLED), 3);
            } else {
                if (!CherryCreakingHeartBlock.isWorldNaturalAndNight(world)) {
                    if (state.get(AbstractCreakingHeartBlock.CREAKING) == AbstractCreakingHeartBlock.Creaking.ACTIVE) {
                        world.setBlockState(pos, state.with(AbstractCreakingHeartBlock.CREAKING, AbstractCreakingHeartBlock.Creaking.DORMANT), 3);
                        return;
                    }
                } else if (state.get(AbstractCreakingHeartBlock.CREAKING) == AbstractCreakingHeartBlock.Creaking.DORMANT) {
                    world.setBlockState(pos, state.with(AbstractCreakingHeartBlock.CREAKING, AbstractCreakingHeartBlock.Creaking.ACTIVE), 3);
                    return;
                }

                if (state.get(AbstractCreakingHeartBlock.CREAKING) == AbstractCreakingHeartBlock.Creaking.ACTIVE) {
                    if (world.getDifficulty() != Difficulty.PEACEFUL) {
                        PlayerEntity playerEntity = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 32.0, false);
                        if (playerEntity != null) {
                            blockEntity.creakingPuppet = spawnCreakingPuppet((ServerWorld)world, blockEntity);
                            if (blockEntity.creakingPuppet != null) {
                                blockEntity.creakingPuppet.playSound(SoundEvents.ENTITY_CREAKING_SPAWN);
                                world.playSound(null, blockEntity.getPos(), SoundEvents.BLOCK_CREAKING_HEART_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }

                    }
                }
            }
        }
    }
}
