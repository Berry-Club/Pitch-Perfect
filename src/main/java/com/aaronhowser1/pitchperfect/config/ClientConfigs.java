package com.aaronhowser1.pitchperfect.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Float> VOLUME;
    public static final ForgeConfigSpec.ConfigValue<Float> MIN_ATTACK_VOLUME;

    static {
        BUILDER.push("Client configs for Pitch Perfect");

        VOLUME = BUILDER
                .comment("The volume that instruments play at, when right-clicked.")
                .define("Instrument Volume", 3F);

        MIN_ATTACK_VOLUME = BUILDER
                .comment("The minimum volume each individual note plays at, when attacking.\nUses the equation:\nMath.max( (1.5*instrumentVolume / amountOfParticles), minimumAttackVolume)")
                .define("Attack Volume", 0.5F);




        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
