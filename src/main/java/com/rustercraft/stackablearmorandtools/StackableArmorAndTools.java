package com.rustercraft.stackablearmorandtools;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(StackableArmorAndTools.MOD_ID)
public final class StackableArmorAndTools {
    public static final String MOD_ID = "stackablearmorandtools";

    public StackableArmorAndTools(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, StackableArmorAndToolsConfig.SPEC);
    }
}
