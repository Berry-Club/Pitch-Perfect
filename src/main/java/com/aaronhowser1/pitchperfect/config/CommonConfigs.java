package com.aaronhowser1.pitchperfect.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MIN_ATTACK_PARTICLES;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_ATTACK_PARTICLES;
    public static final ForgeConfigSpec.ConfigValue<Float> HEAL_AMOUNT;
    public static final ForgeConfigSpec.ConfigValue<Float> HEAL_COOLDOWN_MULT;

    public static final ForgeConfigSpec.ConfigValue<Integer> BWAAAP_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Float> BWAAAP_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> BWAAAP_COOLDOWN_MULT;

    static {
        BUILDER.push("Client configs for Pitch Perfect");

        MIN_ATTACK_PARTICLES = BUILDER
                .comment("The minimum amount of notes to spawn, when attacking.")
                .define("Minimum Attack Particles Amount", 3);
        MAX_ATTACK_PARTICLES = BUILDER
                .comment("The maximum amount of notes to spawn, when attacking.\n(Must be greater than minimum)")
                .define("Maximum Attack Particles Amount", 10);

        HEAL_AMOUNT = BUILDER
                .comment("How much health is healed each time Healing Beat is used.")
                .define("Healing Beat Amount", 0.25F);
        HEAL_COOLDOWN_MULT = BUILDER
                .comment("How many ticks to cool down for every mob healed.\nExample: healing 2 mobs has a default cooldown of 3 ticks.")
                .define("Healing Cooldown Multiplier", 1.5F);

        BWAAAP_RANGE = BUILDER
                .comment("The reach the BWAAAP enchantment has.")
                .define("BWAAAP Range", 5);
        BWAAAP_STRENGTH = BUILDER
                .comment("The strength the BWAAAP enchantment has. Decreases with distance.\nUses the equation:\ntargetDistancePercentageToRange*strength")
                .define("BWAAAP Strength", 1.25F);
        BWAAAP_COOLDOWN_MULT = BUILDER
                .comment("How many ticks to cool down for every mob BWAAAP'd.")
                .define("BWAAAP Cooldown Multiplier", 10);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
