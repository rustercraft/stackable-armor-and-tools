package com.rustercraft.stackablearmorandtools;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.ForgeRegistries;

public final class StackingRules {
    public static final TagKey<Item> STACKABLE_ARMOR = itemTag("stackable_armor");
    public static final TagKey<Item> STACKABLE_TOOLS = itemTag("stackable_tools");
    public static final TagKey<Item> STACKABLE_WEAPONS = itemTag("stackable_weapons");
    public static final TagKey<Item> BLACKLIST = itemTag("blacklist");

    private StackingRules() {
    }

    public static boolean canStack(ItemStack stack) {
        return getCleanStackLimit(stack) > 1;
    }

    public static String rejectionReason(ItemStack stack) {
        if (stack.isEmpty()) {
            return "empty";
        }

        if (stack.is(BLACKLIST)) {
            return "blacklisted";
        }

        if (stack.isDamaged() || stack.getDamageValue() > 0) {
            return "damaged";
        }

        if (stack.isEnchanted()) {
            return "enchanted";
        }

        if (stack.hasCustomHoverName()) {
            return "custom_name";
        }

        CompoundTag tag = stack.getTag();
        if (tag != null) {
            if (StackableArmorAndToolsConfig.BLOCK_ANY_NBT.get() && hasBlockingNbt(tag)) {
                return "nbt";
            }

            if (tag.contains("Enchantments") || tag.contains("StoredEnchantments")) {
                return "enchant_nbt";
            }

            if (tag.contains("display")) {
                return "display_nbt";
            }

            if (tag.contains("Trim")) {
                return "armor_trim";
            }
        }

        Item item = stack.getItem();
        if (!isVanillaItem(item) && !isAllowedModdedTaggedItem(stack)) {
            return "unsupported_modded_item";
        }

        if (getCleanItemStackLimit(item) <= 1) {
            return "disabled_or_unsupported_type";
        }

        return "allowed";
    }

    public static boolean isSupportedStackableItem(Item item) {
        return isVanillaItem(item) && (isSupportedArmor(item) || isSupportedTool(item) || isSupportedWeapon(item) || isOptionalBowsShieldsOrRods(item));
    }

    public static int getCleanItemStackLimit(Item item) {
        if (!isVanillaItem(item)) {
            return 1;
        }

        if (isSupportedArmor(item)) {
            return StackableArmorAndToolsConfig.STACK_ARMOR.get()
                    ? StackableArmorAndToolsConfig.ARMOR_STACK_SIZE.get()
                    : 1;
        }

        if (isSupportedTool(item)) {
            return StackableArmorAndToolsConfig.STACK_TOOLS.get()
                    ? StackableArmorAndToolsConfig.TOOL_STACK_SIZE.get()
                    : 1;
        }

        if (isSupportedWeapon(item)) {
            return StackableArmorAndToolsConfig.STACK_WEAPONS.get()
                    ? StackableArmorAndToolsConfig.WEAPON_STACK_SIZE.get()
                    : 1;
        }

        if (isOptionalBowsShieldsOrRods(item)) {
            return StackableArmorAndToolsConfig.STACK_BOWS_AND_SHIELDS.get()
                    ? StackableArmorAndToolsConfig.WEAPON_STACK_SIZE.get()
                    : 1;
        }

        return 1;
    }

    public static int getCleanStackLimit(ItemStack stack) {
        if (stack.isEmpty() || isUnsafe(stack) || stack.is(BLACKLIST)) {
            return 1;
        }

        Item item = stack.getItem();
        if (!isVanillaItem(item) && !isAllowedModdedTaggedItem(stack)) {
            return 1;
        }

        if (isSupportedArmor(stack)) {
            return getCleanItemStackLimit(item);
        }

        if (isSupportedTool(stack)) {
            return getCleanItemStackLimit(item);
        }

        if (isSupportedWeapon(stack)) {
            return getCleanItemStackLimit(item);
        }

        if (isOptionalBowsShieldsOrRods(item)) {
            return getCleanItemStackLimit(item);
        }

        return 1;
    }

    private static boolean isUnsafe(ItemStack stack) {
        if (stack.isDamaged() || stack.getDamageValue() > 0 || stack.isEnchanted() || stack.hasCustomHoverName()) {
            return true;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return false;
        }

        if (StackableArmorAndToolsConfig.BLOCK_ANY_NBT.get() && hasBlockingNbt(tag)) {
            return true;
        }

        return tag.contains("Enchantments")
                || tag.contains("StoredEnchantments")
                || tag.contains("display")
                || tag.contains("Trim");
    }

    private static boolean hasBlockingNbt(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            if ("Damage".equals(key) && tag.getInt("Damage") == 0) {
                continue;
            }

            if ("ForgeCaps".equals(key) && tag.getCompound("ForgeCaps").isEmpty()) {
                continue;
            }

            return true;
        }

        return false;
    }

    private static boolean isVanillaItem(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        return id != null && "minecraft".equals(id.getNamespace());
    }

    private static boolean isAllowedModdedTaggedItem(ItemStack stack) {
        return StackableArmorAndToolsConfig.ALLOW_MODDED_ITEMS.get()
                && (stack.is(STACKABLE_ARMOR) || stack.is(STACKABLE_TOOLS) || stack.is(STACKABLE_WEAPONS));
    }

    private static boolean isSupportedArmor(ItemStack stack) {
        return isSupportedArmor(stack.getItem()) || stack.is(STACKABLE_ARMOR);
    }

    private static boolean isSupportedArmor(Item item) {
        return item instanceof ArmorItem;
    }

    private static boolean isSupportedTool(ItemStack stack) {
        return isSupportedTool(stack.getItem())
                || stack.is(STACKABLE_TOOLS);
    }

    private static boolean isSupportedTool(Item item) {
        return item instanceof PickaxeItem
                || item instanceof AxeItem
                || item instanceof ShovelItem
                || item instanceof HoeItem;
    }

    private static boolean isSupportedWeapon(ItemStack stack) {
        return isSupportedWeapon(stack.getItem()) || stack.is(STACKABLE_WEAPONS);
    }

    private static boolean isSupportedWeapon(Item item) {
        return item instanceof SwordItem;
    }

    private static boolean isOptionalBowsShieldsOrRods(Item item) {
        return item instanceof BowItem
                || item instanceof CrossbowItem
                || item instanceof ShieldItem
                || item instanceof FishingRodItem;
    }

    private static TagKey<Item> itemTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(StackableArmorAndTools.MOD_ID, path));
    }
}
