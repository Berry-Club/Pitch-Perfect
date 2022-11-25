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
    public static final ForgeConfigSpec.ConfigValue<Long> ELECTRIC_JUMPTIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELECTRIC_MAX_JUMPS;

    static {
        BUILDER.push("Client configs for Pitch Perfect");

        MIN_ATTACK_PARTICLES = BUILDER
                .comment("The minimum amount of notes to spawn, when attacking.")
                .defineInRange("Minimum Attack Particles Amount", 3,0,Integer.MAX_VALUE);
        MAX_ATTACK_PARTICLES = BUILDER
                .comment("The maximum amount of notes to spawn, when attacking.\n(Must be greater than minimum)")
                .defineInRange("Maximum Attack Particles Amount", 10,0,Integer.MAX_VALUE);

        HEAL_AMOUNT = BUILDER
                .comment("How much health is healed each time Healing Beat is used.")
                .defineInRange("Healing Beat Amount", 0.25F, 0F, Float.MAX_VALUE, Float.class);
        HEAL_COOLDOWN_MULT = BUILDER
                .comment("How many ticks to cool down for every mob healed.\nExample: healing 2 mobs has a default cooldown of 3 ticks.")
                .defineInRange("Healing Cooldown Multiplier", 1.5F, 0F, Float.MAX_VALUE, Float.class);

        BWAAAP_RANGE = BUILDER
                .comment("The reach the BWAAAP enchantment has.")
                .defineInRange("BWAAAP Range", 5, 0, Integer.MAX_VALUE);
        BWAAAP_STRENGTH = BUILDER
                .comment("The strength the BWAAAP enchantment has. Decreases with distance.\nUses the equation:\ntargetDistancePercentageToRange*strength")
                .defineInRange("BWAAAP Strength", 1.25F,0F,Float.MAX_VALUE,Float.class);
        BWAAAP_COOLDOWN_MULT = BUILDER
                .comment("How many ticks to cool down for every mob BWAAAP'd.")
                .defineInRange("BWAAAP Cooldown Multiplier", 10, 0, Integer.MAX_VALUE);

        ELECTRIC_RANGE = BUILDER
                .comment("The range in blocks around the attacked mob that should be effected by the \"And His Music Was Electric\" enchantment.")
                .defineInRange("Electric Range", 5, 0, Integer.MAX_VALUE);
        ELECTRIC_DAMAGE_RETURNS = BUILDER
                .comment("The rate of diminishing returns on each mob hit, as a percentage of the original. Uses equation\n originalDamage * ( multiplier / entityNumber )\nwhere entityNumber is how many times it's jumped to a new entity")
                .defineInRange("Electric Damage Multiplier", 0.75F,0F,Float.MAX_VALUE,Float.class);
        ELECTRIC_JUMPTIME = BUILDER
                .comment("How many milliseconds before the lightning jumps to the next entity.")
                .defineInRange("Electric Jumptime", 80L,0,Long.MAX_VALUE);
        ELECTRIC_MAX_JUMPS = BUILDER
                .comment("How many times the lightning can jump.\nKeep in mind that, after a certain amount of jumps, it deals less than half a heart.")
                .defineInRange("Electric Jump Limit", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
