package dev.aaronhowser.pitchperfect.config

import net.minecraftforge.common.ForgeConfigSpec

object CommonConfig {

    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    val MIN_ATTACK_PARTICLES: ForgeConfigSpec.ConfigValue<Int>
    val MAX_ATTACK_PARTICLES: ForgeConfigSpec.ConfigValue<Int>

    init {
        BUILDER.push("Common configs for Pitch Perfect")

        MIN_ATTACK_PARTICLES = BUILDER
            .comment(" The minimum amount of notes to spawn, when attacking.")
            .define("Minimum Attack Particles Amount", 3)

        MAX_ATTACK_PARTICLES = BUILDER
            .comment(" The maximum amount of notes to spawn, when attacking.\n (Must be greater than minimum)")
            .define("Maximum Attack Particles Amount", 10)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }

}