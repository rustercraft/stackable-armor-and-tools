package com.rustercraft.stackablearmorandtools.mixin;

import com.rustercraft.stackablearmorandtools.DebugLog;
import com.rustercraft.stackablearmorandtools.StackingRules;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<AbstractContainerMenu> {
    private CreativeModeInventoryScreenMixin(AbstractContainerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void stackablearmorandtools$mergeCleanCarriedIntoCreativeInventorySlot(Slot slot, int slotId, int button, ClickType clickType, CallbackInfo ci) {
        if (slot == null || this.minecraft == null || this.minecraft.gameMode == null || clickType != ClickType.PICKUP) {
            return;
        }

        ItemStack carried = this.menu.getCarried();
        ItemStack existing = slot.getItem();
        if (carried.isEmpty() || existing.isEmpty() || button > 1) {
            return;
        }

        if (!StackingRules.canStack(carried)
                || !StackingRules.canStack(existing)
                || !ItemStack.isSameItemSameTags(carried, existing)) {
            return;
        }

        int max = Math.min(slot.getMaxStackSize(carried), carried.getMaxStackSize());
        int room = max - existing.getCount();
        if (room <= 0) {
            return;
        }

        int moved = button == 0 ? Math.min(room, carried.getCount()) : 1;
        if (moved <= 0) {
            return;
        }

        ItemStack merged = existing.copy();
        merged.grow(moved);
        carried.shrink(moved);

        slot.setByPlayer(merged);
        slot.setChanged();
        this.menu.setCarried(carried.isEmpty() ? ItemStack.EMPTY : carried);

        int serverSlot = stackablearmorandtools$creativeServerSlot(slot);
        this.minecraft.gameMode.handleCreativeModeItemAdd(merged, serverSlot);
        DebugLog.mergePath("CreativeModeInventoryScreen#slotClicked forcedMerge", existing, carried, true);
        ci.cancel();
    }

    private int stackablearmorandtools$creativeServerSlot(Slot slot) {
        int menuSize = this.menu.slots.size();
        if (slot.index >= 45 && menuSize >= 54) {
            return slot.index - menuSize + 45;
        }

        return slot.index;
    }
}
