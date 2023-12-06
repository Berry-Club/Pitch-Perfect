package com.aaronhowser1.pitchperfect.config

import net.minecraftforge.common.ForgeConfigSpec

object ClientConfig {

    private val BUILDER = ForgeConfigSpec.Builder()

    val SPEC: ForgeConfigSpec
    val VOLUME: ForgeConfigSpec.ConfigValue<Double>
    val MIN_ATTACK_VOLUME: ForgeConfigSpec.ConfigValue<Double>
    val ELECTRIC_PARTICLE_DENSITY: ForgeConfigSpec.ConfigValue<Int>
    val ELECTRIC_PARTICLE_ISWAVE: ForgeConfigSpec.ConfigValue<Boolean>

    init {
        BUILDER.push(" Client configs for Pitch Perfect")

        VOLUME = BUILDER
            .comment(" The volume that instruments play at, when right-clicked.")
            .defineInRange("Instrument Volume", 0.5, 0.0, 3.0)

        MIN_ATTACK_VOLUME = BUILDER
            .comment(" The minimum volume each individual note plays at, when attacking.\n Uses the equation:\n  Math.max( (1.5*instrumentVolume / amountOfParticles), minimumAttackVolume)")
            .defineInRange("Attack Volume", 0.5, 0.0, 3.0)

        ELECTRIC_PARTICLE_DENSITY = BUILDER
            .comment(" How many particles per block should be spawned along the lightning path.\n There's a minimum of 1 tick per particle, so higher numbers may cause the particles to take longer than the jump time.")
            .defineInRange("Electric Particle Density", 10, 1, Int.MAX_VALUE)

        ELECTRIC_PARTICLE_ISWAVE = BUILDER
            .comment(" Whether or not the electric particles are a wave.")
            .define("Electric Particle Is Wave", true)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }

}