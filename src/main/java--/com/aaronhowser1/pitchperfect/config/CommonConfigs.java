package com.aaronhowser1.pitchperfect.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MIN_ATTACK_PARTICLES;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_ATTACK_PARTICLES;



    static {
        BUILDER.push("Common configs for Pitch Perfect");

        MIN_ATTACK_PARTICLES = BUILDER
                .comment(" The minimum amount of notes to spawn, when attacking.")
                .define("Minimum Attack Particles Amount", 3);
        MAX_ATTACK_PARTICLES = BUILDER
                .comment(" The maximum amount of notes to spawn, when attacking.\n (Must be greater than minimum)")
                .define("Maximum Attack Particles Amount", 10);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
