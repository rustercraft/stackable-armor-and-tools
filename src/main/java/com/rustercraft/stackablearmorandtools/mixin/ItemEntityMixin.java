package com.rustercraft.stackablearmorandtools.mixin;

import com.rustercraft.stackablearmorandtools.DebugLog;
import com.rustercraft.stackablearmorandtools.StackingRules;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "isMergable", at = @At("RETURN"))
    private void stackablearmorandtools$isMergable(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.getItem();
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.pathResult("ItemEntity#isMergable", stack, cir.getReturnValue());
        }
    }

    @Inject(method = "tryToMerge", at = @At("HEAD"))
    private void stackablearmorandtools$tryToMerge(ItemEntity other, CallbackInfo ci) {
        ItemStack stack = this.getItem();
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.mergePath("ItemEntity#tryToMerge", stack, other.getItem(), ItemEntity.areMergable(stack, other.getItem()));
        }
    }

    @Inject(method = "areMergable", at = @At("RETURN"))
    private static void stackablearmorandtools$areMergable(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir) {
        if (StackingRules.isSupportedStackableItem(first.getItem())
                || StackingRules.isSupportedStackableItem(second.getItem())) {
            DebugLog.mergePath("ItemEntity#areMergable", first, second, cir.getReturnValue());
        }
    }

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void stackablearmorandtools$playerTouch(Player player, CallbackInfo ci) {
        ItemStack stack = this.getItem();
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.path("ItemEntity#playerTouch", stack);
        }
    }
}
