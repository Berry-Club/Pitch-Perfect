package dev.aaronhowser.mods.pitchperfect

import dev.aaronhowser.mods.pitchperfect.config.ClientConfig
import dev.aaronhowser.mods.pitchperfect.config.CommonConfig
import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.registry.ModRegistries
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runWhenOn

@Mod(PitchPerfect.ID)
class PitchPerfect(
    modContainer: ModContainer
) {

    //TODO:
    // mobs that play music
    // capturing mob souls to play them in your inventory? or use that with the Conductor block? Force a mob to play music?

    companion object {
        const val ID = "pitchperfect"
        val LOGGER: Logger = LogManager.getLogger(ID)
    }

    init {
        ModRegistries.register(MOD_BUS)

        runWhenOn(Dist.CLIENT) {
            val screenFactory = IConfigScreenFactory { container, screen -> ConfigurationScreen(container, screen) }
            modContainer.registerExtensionPoint(IConfigScreenFactory::class.java, screenFactory)
        }

        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC)
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)

    }
}