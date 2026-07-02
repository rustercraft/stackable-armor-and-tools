package com.rustercraft.stackablearmorandtools.mixin;

import com.rustercraft.stackablearmorandtools.DebugLog;
import com.rustercraft.stackablearmorandtools.StackingRules;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"))
    private void stackablearmorandtools$add(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.path("Inventory#add", stack);
        }
    }

    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"))
    private void stackablearmorandtools$addSlot(int slotId, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.path("Inventory#add(slot=" + slotId + ")", stack);
        }
    }

    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"))
    private void stackablearmorandtools$addSlotReturn(int slotId, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.pathResult("Inventory#add(slot=" + slotId + ") return", stack, cir.getReturnValue());
        }
    }

    @Inject(method = "addResource(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"))
    private void stackablearmorandtools$addResource(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.path("Inventory#addResource", stack);
        }
    }

    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"))
    private void stackablearmorandtools$addResourceSlot(int slotId, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.path("Inventory#addResource(slot=" + slotId + ")", stack);
        }
    }
}
