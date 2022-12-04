package com.aaronhowser1.pitchperfect.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Double> VOLUME;
    public static final ForgeConfigSpec.ConfigValue<Double> MIN_ATTACK_VOLUME;

    static {
        BUILDER.push(" Client configs for Pitch Perfect");

        VOLUME = BUILDER
                .comment(" The volume that instruments play at, when right-clicked.")
                .defineInRange("Instrument Volume", 2F, 0F, 3F);

        MIN_ATTACK_VOLUME = BUILDER
                .comment(" The minimum volume each individual note plays at, when attacking.\nUses the equation:\nMath.max( (1.5*instrumentVolume / amountOfParticles), minimumAttackVolume)")
                .defineInRange("Attack Volume", 0.5F, 0F, 3F);




        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
