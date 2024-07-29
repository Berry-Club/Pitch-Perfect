package dev.aaronhowser.mods.pitchperfect.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair


class ClientConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<ClientConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ClientConfig)

        val CONFIG: ClientConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var VOLUME: ModConfigSpec.DoubleValue
        lateinit var ELECTRIC_PARTICLE_DENSITY: ModConfigSpec.IntValue
        lateinit var ELECTRIC_PARTICLE_IS_WAVE: ModConfigSpec.BooleanValue

        lateinit var LOG_MISSING_NOTE: ModConfigSpec.BooleanValue
    }

    init {
        clientConfigs()
        builder.build()
    }

    private fun clientConfigs() {

        builder.push("Client")

        VOLUME = builder
            .comment("The volume that instruments play at, when right-clicked.")
            .defineInRange("Instrument Volume", 0.5, 0.0, 3.0)

        ELECTRIC_PARTICLE_DENSITY = builder
            .comment("How many particles per block should be spawned along the lightning path.\nThere's a minimum of 1 tick per particle, so higher numbers may cause the particles to take longer than the jump time.")
            .defineInRange("Electric Particle Density", 10, 1, Int.MAX_VALUE)

        ELECTRIC_PARTICLE_IS_WAVE = builder
            .comment("Whether or not the electric particles are a wave.")
            .define("Electric Particle Is Wave", true)

        LOG_MISSING_NOTE = builder
            .comment("Whether or not to log a missing note when the mod doesn't find an exact match for a Note's pitch, and uses the closest Note instead.")
            .define("Log Missing Note", false)

        builder.pop()

    }


}