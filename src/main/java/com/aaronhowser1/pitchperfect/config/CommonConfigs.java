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

    public static final ForgeConfigSpec.ConfigValue<Integer> ELECTRIC_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Float> ELECTRIC_DAMAGE_RETURNS;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELECTRIC_JUMPTIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELECTRIC_MAX_JUMPS;

    static {
        BUILDER.push("Common configs for Pitch Perfect");

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

        ELECTRIC_RANGE = BUILDER
                .comment("The range in blocks around the attacked mob that should be effected by the \"And His Music Was Electric\" enchantment.")
                .define("Electric Range", 5);
        ELECTRIC_DAMAGE_RETURNS = BUILDER
                .comment("The rate of diminishing returns on each mob hit, as a percentage of the original. Uses equation\n originalDamage * ( multiplier / entityNumber )\nwhere entityNumber is how many times it's jumped to a new entity")
                .define("Electric Damage Multiplier", 0.75F);
        ELECTRIC_JUMPTIME = BUILDER
                .comment("How many milliseconds before the lightning jumps to the next entity.")
                .define("Electric Jumptime", 3000);
        ELECTRIC_MAX_JUMPS = BUILDER
                .comment("How many times the lightning can jump.\nKeep in mind that, after a certain amount of jumps, it deals less than half a heart.")
                .defineInRange("Electric Jump Limit", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
