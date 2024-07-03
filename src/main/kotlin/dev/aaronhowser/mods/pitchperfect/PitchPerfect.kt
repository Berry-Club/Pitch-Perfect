package dev.aaronhowser.mods.pitchperfect

import dev.aaronhowser.mods.pitchperfect.config.ClientConfig
import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.registry.ModRegistries
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(PitchPerfect.ID)
class PitchPerfect(
    modContainer: ModContainer
) {

    companion object {
        const val ID = "pitchperfect"
        val LOGGER: Logger = LogManager.getLogger(ID)
    }

    init {

        ModRegistries.register(MOD_BUS)

        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)

    }
}