package dev.aaronhowser.mods.pitchperfect.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair


class CommonConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<CommonConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::CommonConfig)

        val CONFIG: CommonConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var MIN_ATTACK_PARTICLES: ModConfigSpec.IntValue
        lateinit var MAX_ATTACK_PARTICLES: ModConfigSpec.IntValue

    }

    init {
        clientConfigs()
        builder.build()
    }

    private fun clientConfigs() {

        builder.push("Common")

        MIN_ATTACK_PARTICLES = builder
            .comment("The minimum amount of notes to spawn, when attacking.")
            .defineInRange("Minimum Attack Particles Amount", 3, 0, Int.MAX_VALUE)

        MAX_ATTACK_PARTICLES = builder
            .comment("The maximum amount of notes to spawn, when attacking.\n(Must be greater than minimum)")
            .defineInRange("Maximum Attack Particles Amount", 10, 0, Int.MAX_VALUE)

        builder.pop()

    }


}