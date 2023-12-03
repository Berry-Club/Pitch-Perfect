package com.aaronhowser1.pitchperfect;

import com.aaronhowser1.pitchperfect.config.ClientConfigs;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import com.aaronhowser1.pitchperfect.config.ServerConfigs;
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments;
import com.aaronhowser1.pitchperfect.item.ModItems;
import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PitchPerfect.MOD_ID)
public class PitchPerfect
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "pitchperfect";

    public PitchPerfect() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigs.SPEC, "pitchperfect-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC, "pitchperfect-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfigs.SPEC, "pitchperfect-server.toml");

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModEnchantments.register(eventBus);
        ModSounds.register(eventBus);

        ModPacketHandler.setup();

        MinecraftForge.EVENT_BUS.register(this);
    }

}