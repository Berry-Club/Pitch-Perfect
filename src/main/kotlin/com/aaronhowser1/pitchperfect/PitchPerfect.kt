package com.aaronhowser1.pitchperfect

import com.aaronhowser1.pitchperfect.config.ClientConfig
import com.aaronhowser1.pitchperfect.config.CommonConfig
import com.aaronhowser1.pitchperfect.config.ServerConfig
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(PitchPerfect.MOD_ID)
object PitchPerfect {

    const val MOD_ID = "pitchperfect"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    init {
        LOGGER.log(Level.INFO, "Pitch Perfect loaded!")
    }

    init {
        LOGGER.log(Level.INFO, "Pitch Perfect loaded!")

        ModLoadingContext.get().apply {
            registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "pitchperfect-client.toml")
            registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "pitchperfect-common.toml")
            registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, "pitchperfect-server.toml")
        }
    }
}