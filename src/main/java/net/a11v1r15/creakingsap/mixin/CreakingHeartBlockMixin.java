package net.a11v1r15.creakingsap.mixin;

import net.a11v1r15.creakingsap.CreakingSap;
import net.minecraft.block.Block;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CreakingHeartBlock.class)
public class CreakingHeartBlockMixin {
    private static TagKey<Block> getWood() {
        return BlockTags.PALE_OAK_LOGS;
    }

    @ModifyArg(method = "shouldBeEnabled(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"), index = 0)
    private static TagKey<Block> CreakingSap$ChangeTagToFunction(TagKey<Block> par1) {
        return getWood();
    }
}
