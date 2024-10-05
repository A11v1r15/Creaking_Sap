package net.a11v1r15.creakingsap.mixin;

import net.a11v1r15.creakingsap.CreakingSapBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

    @Inject(method = "supports", at = @At("HEAD"), cancellable = true)
    private void CreakingSap$BlockEntityTypeWorkaround(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (BlockEntityType.CREAKING_HEART.equals(this))
            if( state.isOf(CreakingSapBlocks.BIRCH_CREAKING_HEART) ||
                state.isOf(CreakingSapBlocks.OAK_CREAKING_HEART) ||
                state.isOf(CreakingSapBlocks.DARK_OAK_CREAKING_HEART) ||
                state.isOf(CreakingSapBlocks.SPRUCE_CREAKING_HEART) ||
                state.isOf(CreakingSapBlocks.JUNGLE_CREAKING_HEART)) {
                    cir.setReturnValue(true);
        }
    }
}