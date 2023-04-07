package com.aaronhowser1.pitchperfect.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Double> VOLUME;
    public static final ForgeConfigSpec.ConfigValue<Double> MIN_ATTACK_VOLUME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELECTRIC_PARTICLE_DENSITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ELECTRIC_PARTICLE_ISWAVE;


    static {
        BUILDER.push(" Client configs for Pitch Perfect");

        VOLUME = BUILDER
                .comment(" The volume that instruments play at, when right-clicked.")
                .defineInRange("Instrument Volume", 0.5F, 0F, 3F);

        MIN_ATTACK_VOLUME = BUILDER
                .comment(" The minimum volume each individual note plays at, when attacking.\n Uses the equation:\n  Math.max( (1.5*instrumentVolume / amountOfParticles), minimumAttackVolume)")
                .defineInRange("Attack Volume", 0.5F, 0F, 3F);

        ELECTRIC_PARTICLE_DENSITY = BUILDER
                .comment(" How many particles per block should be spawned along the lightning path.\n There's a minimum of 1 tick per particle, so higher numbers may cause the particles to take longer than the jump time.")
                .defineInRange("Electric Particle Density", 3, 0, Integer.MAX_VALUE);
        ELECTRIC_PARTICLE_ISWAVE = BUILDER
                .comment(" Should the wave of particles from mob A to mob B be a wave?\n False instead spawns a line, which spawns them all at once.")
                .define("Electric Particle is wave", true);



        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
