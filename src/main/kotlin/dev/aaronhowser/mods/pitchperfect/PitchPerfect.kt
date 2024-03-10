package dev.aaronhowser.mods.pitchperfect

import dev.aaronhowser.mods.pitchperfect.config.ClientConfig
import dev.aaronhowser.mods.pitchperfect.config.CommonConfig
import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.enchantment.ModEnchantments
import dev.aaronhowser.mods.pitchperfect.item.ModItems
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(PitchPerfect.MOD_ID)
object PitchPerfect {

    const val MOD_ID = "pitchperfect"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    init {
        LOGGER.log(Level.INFO, "Pitch Perfect loaded!")

        ModLoadingContext.get().apply {
            registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "pitchperfect-client.toml")
            registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "pitchperfect-common.toml")
            registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, "pitchperfect-server.toml")
        }


        ModItems.register(MOD_BUS)
        ModEnchantments.register(MOD_BUS)
        ModSounds.register(MOD_BUS)

        ModPacketHandler.setup()

        MinecraftForge.EVENT_BUS.register(this)

    }
}