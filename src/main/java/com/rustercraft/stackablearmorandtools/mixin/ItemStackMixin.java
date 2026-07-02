package com.rustercraft.stackablearmorandtools.mixin;

import com.rustercraft.stackablearmorandtools.DebugLog;
import com.rustercraft.stackablearmorandtools.StackingRules;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    private static boolean stackablearmorandtools$loggedActive;

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void stackablearmorandtools$getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (!stackablearmorandtools$loggedActive) {
            stackablearmorandtools$loggedActive = true;
            DebugLog.mixinActive("ItemStackMixin");
        }

        ItemStack stack = (ItemStack) (Object) this;
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            int cleanStackLimit = StackingRules.getCleanStackLimit(stack);
            DebugLog.stackDecision("ItemStack#getMaxStackSize", stack, cleanStackLimit, cleanStackLimit > 1);
            cir.setReturnValue(cleanStackLimit);
        }
    }

    @Inject(method = "isStackable", at = @At("HEAD"), cancellable = true)
    private void stackablearmorandtools$isStackable(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        boolean canStack = StackingRules.canStack(stack);
        if (canStack) {
            DebugLog.stackableDecision(stack, true);
            cir.setReturnValue(true);
        }
    }
}
