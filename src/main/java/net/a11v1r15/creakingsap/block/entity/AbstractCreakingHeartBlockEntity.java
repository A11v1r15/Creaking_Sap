package net.a11v1r15.creakingsap.block.entity;

import net.minecraft.block.BlockState;
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

public class AbstractCreakingHeartBlockEntity extends BlockEntity {
    @Nullable
    TransientCreakingEntity creakingPuppet;
    int creakingUpdateTimer;
    int trailParticlesSpawnTimer;
    @Nullable
    Vec3d lastCreakingPuppetPos;

    public AbstractCreakingHeartBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityType.CREAKING_HEART, pos, state);
    }

    @Nullable
    static TransientCreakingEntity spawnCreakingPuppet(ServerWorld world, AbstractCreakingHeartBlockEntity blockEntity) {
        BlockPos blockPos = blockEntity.getPos();
        Optional<TransientCreakingEntity> optional = LargeEntitySpawnHelper.trySpawnAt(EntityType.CREAKING_TRANSIENT, SpawnReason.SPAWNER, world, blockPos, 5, 16, 8, LargeEntitySpawnHelper.Requirements.CREAKING);
        if (optional.isEmpty()) {
            return null;
        } else {
            TransientCreakingEntity transientCreakingEntity = optional.get();
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
            if (var2 instanceof ServerWorld serverWorld) {
                this.spawnTrailParticles(serverWorld, 20, false);
                this.trailParticlesSpawnTimer = 100;
                this.lastCreakingPuppetPos = this.creakingPuppet.getBoundingBox().getCenter();
            }
        }
    }

    void spawnTrailParticles(ServerWorld world, int count, boolean bl) {
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
