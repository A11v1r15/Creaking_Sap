package net.a11v1r15.creakingsap.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.mob.TransientCreakingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.TrailParticleEffect;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AbstractCreakingHeartBlockEntity  extends BlockEntity {
    @Nullable
    private TransientCreakingEntity creakingPuppet;
    private int creakingUpdateTimer;
    private int trailParticlesSpawnTimer;
    @Nullable
    private Vec3d lastCreakingPuppetPos;

    public AbstractCreakingHeartBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityType.CREAKING_HEART, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, AbstractCreakingHeartBlockEntity blockEntity) {
        if (blockEntity.trailParticlesSpawnTimer > 0) {
            if (blockEntity.trailParticlesSpawnTimer > 50) {
                blockEntity.spawnTrailParticles((ServerWorld)world, 1, true);
                blockEntity.spawnTrailParticles((ServerWorld)world, 1, false);
            }

            if (blockEntity.trailParticlesSpawnTimer % 10 == 0 && world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)world;
                if (blockEntity.lastCreakingPuppetPos != null) {
                    if (blockEntity.creakingPuppet != null) {
                        blockEntity.lastCreakingPuppetPos = blockEntity.creakingPuppet.getBoundingBox().getCenter();
                    }

                    Vec3d vec3d = Vec3d.ofCenter(pos);
                    float f = 0.2F + 0.8F * (float)(100 - blockEntity.trailParticlesSpawnTimer) / 100.0F;
                    Vec3d vec3d2 = vec3d.subtract(blockEntity.lastCreakingPuppetPos).multiply((double)f).add(blockEntity.lastCreakingPuppetPos);
                    BlockPos blockPos = BlockPos.ofFloored(vec3d2);
                    float g = (float)blockEntity.trailParticlesSpawnTimer / 2.0F / 100.0F + 0.5F;
                    serverWorld.playSound((PlayerEntity)null, blockPos, SoundEvents.BLOCK_CREAKING_HEART_HURT, SoundCategory.BLOCKS, g, 1.0F);
                }
            }

            --blockEntity.trailParticlesSpawnTimer;
        }

        if (blockEntity.creakingUpdateTimer-- < 0) {
            blockEntity.creakingUpdateTimer = 20;
            if (blockEntity.creakingPuppet != null) {
                if (CreakingHeartBlock.isWorldNaturalAndNight(world) && !(blockEntity.creakingPuppet.squaredDistanceTo(Vec3d.ofBottomCenter(pos)) > 1156.0)) {
                    if (blockEntity.creakingPuppet.isRemoved()) {
                        blockEntity.creakingPuppet = null;
                    }

                    if (!CreakingHeartBlock.shouldBeEnabled(state, world, pos) && blockEntity.creakingPuppet == null) {
                        world.setBlockState(pos, (BlockState)state.with(CreakingHeartBlock.CREAKING, CreakingHeartBlock.Creaking.DISABLED), 3);
                    }

                } else {
                    blockEntity.onBreak((DamageSource)null);
                }
            } else if (!CreakingHeartBlock.shouldBeEnabled(state, world, pos)) {
                world.setBlockState(pos, (BlockState)state.with(CreakingHeartBlock.CREAKING, CreakingHeartBlock.Creaking.DISABLED), 3);
            } else {
                if (!CreakingHeartBlock.isWorldNaturalAndNight(world)) {
                    if (state.get(CreakingHeartBlock.CREAKING) == CreakingHeartBlock.Creaking.ACTIVE) {
                        world.setBlockState(pos, (BlockState)state.with(CreakingHeartBlock.CREAKING, CreakingHeartBlock.Creaking.DORMANT), 3);
                        return;
                    }
                } else if (state.get(CreakingHeartBlock.CREAKING) == CreakingHeartBlock.Creaking.DORMANT) {
                    world.setBlockState(pos, (BlockState)state.with(CreakingHeartBlock.CREAKING, CreakingHeartBlock.Creaking.ACTIVE), 3);
                    return;
                }

                if (state.get(CreakingHeartBlock.CREAKING) == CreakingHeartBlock.Creaking.ACTIVE) {
                    if (world.getDifficulty() != Difficulty.PEACEFUL) {
                        PlayerEntity playerEntity = world.getClosestPlayer((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 32.0, false);
                        if (playerEntity != null) {
                            blockEntity.creakingPuppet = spawnCreakingPuppet((ServerWorld)world, blockEntity);
                            if (blockEntity.creakingPuppet != null) {
                                blockEntity.creakingPuppet.playSound(SoundEvents.ENTITY_CREAKING_SPAWN);
                                world.playSound((PlayerEntity)null, blockEntity.getPos(), SoundEvents.BLOCK_CREAKING_HEART_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }

                    }
                }
            }
        }
    }

    @Nullable
    private static TransientCreakingEntity spawnCreakingPuppet(ServerWorld world, AbstractCreakingHeartBlockEntity blockEntity) {
        BlockPos blockPos = blockEntity.getPos();
        Optional<TransientCreakingEntity> optional = LargeEntitySpawnHelper.trySpawnAt(EntityType.CREAKING_TRANSIENT, SpawnReason.SPAWNER, world, blockPos, 5, 16, 8, LargeEntitySpawnHelper.Requirements.CREAKING);
        if (optional.isEmpty()) {
            return null;
        } else {
            TransientCreakingEntity transientCreakingEntity = (TransientCreakingEntity)optional.get();
            world.emitGameEvent(transientCreakingEntity, GameEvent.ENTITY_PLACE, transientCreakingEntity.getPos());
            transientCreakingEntity.playSpawnEffects();
            transientCreakingEntity.setHeartPos(blockPos);
            return transientCreakingEntity;
        }
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return this.createComponentlessNbt(registries);
    }

    public void onPuppetDamage() {
        if (this.creakingPuppet != null) {
            World var2 = this.world;
            if (var2 instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)var2;
                this.spawnTrailParticles(serverWorld, 20, false);
                this.trailParticlesSpawnTimer = 100;
                this.lastCreakingPuppetPos = this.creakingPuppet.getBoundingBox().getCenter();
            }
        }
    }

    private void spawnTrailParticles(ServerWorld world, int count, boolean bl) {
        if (this.creakingPuppet != null) {
            int i = bl ? 16545810 : 6250335;
            Random random = world.random;

            for(double d = 0.0; d < (double)count; ++d) {
                Vec3d vec3d = this.creakingPuppet.getBoundingBox().getMinPos().add(random.nextDouble() * this.creakingPuppet.getBoundingBox().getLengthX(), random.nextDouble() * this.creakingPuppet.getBoundingBox().getLengthY(), random.nextDouble() * this.creakingPuppet.getBoundingBox().getLengthZ());
                Vec3d vec3d2 = Vec3d.of(this.getPos()).add(random.nextDouble(), random.nextDouble(), random.nextDouble());
                if (bl) {
                    Vec3d vec3d3 = vec3d;
                    vec3d = vec3d2;
                    vec3d2 = vec3d3;
                }

                TrailParticleEffect trailParticleEffect = new TrailParticleEffect(vec3d2, i);
                world.spawnParticles(trailParticleEffect, vec3d.x, vec3d.y, vec3d.z, 1, 0.0, 0.0, 0.0, 0.0);
            }

        }
    }

    public void onBreak(@Nullable DamageSource damageSource) {
        if (this.creakingPuppet != null) {
            this.creakingPuppet.damageFromHeart(damageSource);
            this.creakingPuppet = null;
        }

    }

    public boolean isPuppet(CreakingEntity creaking) {
        return this.creakingPuppet == creaking;
    }
}
