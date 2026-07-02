package com.rustercraft.stackablearmorandtools.mixin;

import com.rustercraft.stackablearmorandtools.DebugLog;
import com.rustercraft.stackablearmorandtools.StackingRules;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Inject(method = "getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void stackablearmorandtools$getMaxStackSize(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            Slot slot = (Slot) (Object) this;
            int limit = Math.min(slot.getMaxStackSize(), StackingRules.getCleanStackLimit(stack));
            DebugLog.slotPath("Slot#getMaxStackSize(ItemStack)", slot, stack, limit);
            cir.setReturnValue(limit);
        }
    }

    @Inject(method = "safeInsert(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"))
    private void stackablearmorandtools$safeInsert(ItemStack stack, int count, CallbackInfoReturnable<ItemStack> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.slotPath("Slot#safeInsert", (Slot) (Object) this, stack, StackingRules.getCleanStackLimit(stack));
        }
    }
}
