package com.rustercraft.stackablearmorandtools;

import net.minecraftforge.common.ForgeConfigSpec;

public final class StackableArmorAndToolsConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DEBUG_LOGGING = BUILDER
            .comment("Log stackability decisions. This is noisy and should stay disabled unless debugging.")
            .define("debugLogging", false);

    public static final ForgeConfigSpec.IntValue ARMOR_STACK_SIZE = BUILDER
            .comment("Maximum stack size for clean supported armor.")
            .defineInRange("armorStackSize", 16, 1, 64);

    public static final ForgeConfigSpec.IntValue TOOL_STACK_SIZE = BUILDER
            .comment("Maximum stack size for clean supported tools.")
            .defineInRange("toolStackSize", 16, 1, 64);

    public static final ForgeConfigSpec.IntValue WEAPON_STACK_SIZE = BUILDER
            .comment("Maximum stack size for clean supported weapons.")
            .defineInRange("weaponStackSize", 16, 1, 64);

    public static final ForgeConfigSpec.BooleanValue STACK_ARMOR = BUILDER
            .comment("Allow clean supported armor to stack.")
            .define("stackArmor", true);

    public static final ForgeConfigSpec.BooleanValue STACK_TOOLS = BUILDER
            .comment("Allow clean supported tools to stack.")
            .define("stackTools", true);

    public static final ForgeConfigSpec.BooleanValue STACK_WEAPONS = BUILDER
            .comment("Allow clean supported swords to stack.")
            .define("stackWeapons", true);

    public static final ForgeConfigSpec.BooleanValue STACK_BOWS_AND_SHIELDS = BUILDER
            .comment("Allow clean bows, crossbows, shields, and fishing rods to stack.")
            .define("stackBowsAndShields", false);

    public static final ForgeConfigSpec.BooleanValue ALLOW_MODDED_ITEMS = BUILDER
            .comment("Allow non-Minecraft items only when they are explicitly included by this mod's item tags.")
            .define("allowModdedItems", false);

    public static final ForgeConfigSpec.BooleanValue BLOCK_ANY_NBT = BUILDER
            .comment("Keep any item with an NBT tag unstackable. Leave enabled for safest behavior.")
            .define("blockAnyNbt", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private StackableArmorAndToolsConfig() {
    }
}
