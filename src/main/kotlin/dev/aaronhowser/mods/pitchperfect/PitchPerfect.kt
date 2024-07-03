package dev.aaronhowser.mods.pitchperfect

import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(PitchPerfect.ID)
class PitchPerfect(
    modContainer: ModContainer
) {

    companion object {
        const val ID = "pitchperfect"
        val LOGGER: Logger = LogManager.getLogger(ID)
    }

    init {

    }
}