package com.rustercraft.stackablearmorandtools.mixin;

import com.rustercraft.stackablearmorandtools.DebugLog;
import com.rustercraft.stackablearmorandtools.StackingRules;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    private static boolean stackablearmorandtools$loggedActive;

    @Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
    private void stackablearmorandtools$getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (!stackablearmorandtools$loggedActive) {
            stackablearmorandtools$loggedActive = true;
            DebugLog.mixinActive("ItemMixin");
        }

        Item item = (Item) (Object) this;
        int cleanItemStackLimit = StackingRules.getCleanItemStackLimit(item);
        if (cleanItemStackLimit > 1) {
            DebugLog.itemMaxStackSize(item, cleanItemStackLimit);
            cir.setReturnValue(cleanItemStackLimit);
        }
    }
}
