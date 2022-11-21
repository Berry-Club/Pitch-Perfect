package com.aaronhowser1.pitchperfect.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Float> HEAL_AMOUNT;
    public static final ForgeConfigSpec.ConfigValue<Float> HEAL_COOLDOWN_MULT;

    static {
        BUILDER.push("Client configs for Pitch Perfect");

        HEAL_AMOUNT = BUILDER
                .comment("How much health is healed each time Healing Beat is used.")
                .define("Healing Beat Amount", 0.25F);
        HEAL_COOLDOWN_MULT = BUILDER
                .comment("How many ticks to cool down for every mob healed.\nExample: healing 2 mobs has a default cooldown of 3 ticks.")
                .define("Healing Cooldown Multiplier", 1.5F);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
