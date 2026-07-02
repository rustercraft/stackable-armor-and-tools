package com.rustercraft.stackablearmorandtools;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

public final class DebugLog {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PREFIX = "[StackableArmorAndTools DEBUG]";

    private DebugLog() {
    }

    public static void mixinActive(String mixinName) {
        log("{} mixin active", mixinName);
    }

    public static void itemMaxStackSize(Item item, int stackLimit) {
        if (!enabled()) {
            return;
        }

        log("Item#getMaxStackSize item={} armor={} tool={} weapon={} finalStackSize={}",
                itemId(item),
                item instanceof ArmorItem,
                isTool(item),
                item instanceof SwordItem,
                stackLimit);
    }

    public static void stackDecision(String method, ItemStack stack, int stackLimit, boolean canStack) {
        if (!enabled()) {
            return;
        }

        CompoundTag tag = stack.getTag();
        log("{} item={} count={} armor={} tool={} weapon={} hasNbt={} nbtKeys={} damaged={} damage={} enchanted={} customName={} trim={} canStack={} reason={} finalStackSize={}",
                method,
                itemId(stack.getItem()),
                stack.getCount(),
                stack.getItem() instanceof ArmorItem,
                isTool(stack.getItem()),
                stack.getItem() instanceof SwordItem,
                tag != null,
                tag != null ? tag.getAllKeys() : "[]",
                stack.isDamaged(),
                stack.getDamageValue(),
                stack.isEnchanted(),
                stack.hasCustomHoverName(),
                tag != null && tag.contains("Trim"),
                canStack,
                StackingRules.rejectionReason(stack),
                stackLimit);
    }

    public static void stackableDecision(ItemStack stack, boolean canStack) {
        if (!enabled()) {
            return;
        }

        CompoundTag tag = stack.getTag();
        log("ItemStack#isStackable item={} armor={} tool={} weapon={} hasNbt={} damaged={} damage={} enchanted={} customName={} trim={} canStack={} finalBoolean={}",
                itemId(stack.getItem()),
                stack.getItem() instanceof ArmorItem,
                isTool(stack.getItem()),
                stack.getItem() instanceof SwordItem,
                tag != null,
                stack.isDamaged(),
                stack.getDamageValue(),
                stack.isEnchanted(),
                stack.hasCustomHoverName(),
                tag != null && tag.contains("Trim"),
                canStack,
                canStack);
    }

    public static void path(String method, ItemStack stack) {
        if (!enabled()) {
            return;
        }

        log("{} {}", method, stackSummary(stack));
    }

    public static void pathResult(String method, ItemStack stack, boolean result) {
        if (!enabled()) {
            return;
        }

        log("{} {} result={}", method, stackSummary(stack), result);
    }

    public static void mergePath(String method, ItemStack first, ItemStack second, boolean result) {
        if (!enabled()) {
            return;
        }

        log("{} first=[{}] second=[{}] result={}", method, stackSummary(first), stackSummary(second), result);
    }

    public static void menuPath(String method, int slotId, int button, ClickType clickType, ItemStack carried, ItemStack slotStack) {
        if (!enabled()) {
            return;
        }

        log("{} slot={} button={} clickType={} carried=[{}] slotStack=[{}]",
                method,
                slotId,
                button,
                clickType,
                stackSummary(carried),
                stackSummary(slotStack));
    }

    public static void slotPath(String method, Slot slot, ItemStack stack, int limit) {
        if (!enabled()) {
            return;
        }

        log("{} slotIndex={} containerSlot={} {} finalSlotLimit={}",
                method,
                slot.index,
                slot.getContainerSlot(),
                stackSummary(stack),
                limit);
    }

    public static boolean isDebugEnabled() {
        return enabled();
    }

    private static void log(String message, Object... args) {
        if (enabled()) {
            LOGGER.info(PREFIX + " " + message, args);
        }
    }

    private static String stackSummary(ItemStack stack) {
        if (stack == null) {
            return "null";
        }

        CompoundTag tag = stack.getTag();
        int limit = 1;
        boolean canStack = false;
        String reason = "unknown";
        try {
            limit = StackingRules.getCleanStackLimit(stack);
            canStack = StackingRules.canStack(stack);
            reason = StackingRules.rejectionReason(stack);
        } catch (IllegalStateException ignored) {
            // Config may not be ready during early bootstrap logging.
        }

        return "item=" + itemId(stack.getItem())
                + " count=" + stack.getCount()
                + " hasNbt=" + (tag != null)
                + " nbtKeys=" + (tag != null ? tag.getAllKeys() : "[]")
                + " damaged=" + stack.isDamaged()
                + " damage=" + stack.getDamageValue()
                + " enchanted=" + stack.isEnchanted()
                + " customName=" + stack.hasCustomHoverName()
                + " trim=" + (tag != null && tag.contains("Trim"))
                + " canStack=" + canStack
                + " reason=" + reason
                + " finalMax=" + limit;
    }

    private static boolean enabled() {
        try {
            return StackableArmorAndToolsConfig.DEBUG_LOGGING.get();
        } catch (IllegalStateException ignored) {
            return false;
        }
    }

    private static boolean isTool(Item item) {
        return item instanceof PickaxeItem
                || item instanceof AxeItem
                || item instanceof ShovelItem
                || item instanceof HoeItem;
    }

    private static ResourceLocation itemId(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
