package com.rustercraft.stackablearmorandtools.mixin;

import com.rustercraft.stackablearmorandtools.DebugLog;
import com.rustercraft.stackablearmorandtools.StackingRules;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow
    public NonNullList<Slot> slots;

    @Shadow
    public abstract ItemStack getCarried();

    @Inject(method = "doClick", at = @At("HEAD"))
    private void stackablearmorandtools$doClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        if (!DebugLog.isDebugEnabled()) {
            return;
        }

        ItemStack slotStack = ItemStack.EMPTY;
        if (slotId >= 0 && slotId < this.slots.size()) {
            slotStack = this.slots.get(slotId).getItem();
        }

        if (StackingRules.isSupportedStackableItem(this.getCarried().getItem())
                || StackingRules.isSupportedStackableItem(slotStack.getItem())) {
            DebugLog.menuPath("AbstractContainerMenu#doClick", slotId, button, clickType, this.getCarried(), slotStack);
        }
    }

    @Inject(method = "moveItemStackTo", at = @At("HEAD"))
    private void stackablearmorandtools$moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, CallbackInfoReturnable<Boolean> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.path("AbstractContainerMenu#moveItemStackTo startIndex=" + startIndex + " endIndex=" + endIndex + " reverse=" + reverseDirection, stack);
        }
    }

    @Inject(method = "moveItemStackTo", at = @At("RETURN"))
    private void stackablearmorandtools$moveItemStackToReturn(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, CallbackInfoReturnable<Boolean> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.pathResult("AbstractContainerMenu#moveItemStackTo return", stack, cir.getReturnValue());
        }
    }

    @Inject(method = "canItemQuickReplace", at = @At("RETURN"))
    private static void stackablearmorandtools$canItemQuickReplace(Slot slot, ItemStack stack, boolean allowOverflow, CallbackInfoReturnable<Boolean> cir) {
        if (StackingRules.isSupportedStackableItem(stack.getItem())) {
            DebugLog.pathResult("AbstractContainerMenu#canItemQuickReplace allowOverflow=" + allowOverflow, stack, cir.getReturnValue());
        }
    }
}
