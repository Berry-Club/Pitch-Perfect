package com.aaronhowser1.pitchperfect.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ServerConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Lifted largely from Eccentric Tome, as that was the first config I found that had lists

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HEALING_BEAT_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HEALING_BEAT_BLACKLIST;

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
        BUILDER.push("Server configs for Pitch Perfect");

        HEAL_AMOUNT = BUILDER
                .comment(" How much health is healed each time Healing Beat is used.")
                .define("Healing Beat Amount", 0.25F);
        HEAL_COOLDOWN_MULT = BUILDER
                .comment(" How many ticks to cool down for every mob healed.\n Example: healing 2 mobs has a default cooldown of 3 ticks.")
                .define("Healing Cooldown Multiplier", 1.5F);
        HEALING_BEAT_WHITELIST = BUILDER
                .comment(" Mobs that Healing Beat can always heal\n Example: [\"minecraft:piglin\"]")
                .defineListAllowEmpty(List.of("Healing Beat Whitelist"), List::of, Validator::isStringResource);

        HEALING_BEAT_BLACKLIST = BUILDER
                .comment(" Mobs that Healing Beat can never heal\n Example: [\"minecraft:cow\"]")
                .defineListAllowEmpty(List.of("Healing Beat Blacklist"), List::of, Validator::isStringResource);

        BWAAAP_RANGE = BUILDER
                .comment(" The reach the BWAAAP enchantment has.")
                .define("BWAAAP Range", 5);
        BWAAAP_STRENGTH = BUILDER
                .comment(" The strength the BWAAAP enchantment has. Decreases with distance.\n Uses the equation:\n targetDistancePercentageToRange*strength")
                .define("BWAAAP Strength", 1.25F);
        BWAAAP_COOLDOWN_MULT = BUILDER
                .comment(" How many ticks to cool down for every mob BWAAAP'd.")
                .define("BWAAAP Cooldown Multiplier", 10);

        ELECTRIC_RANGE = BUILDER
                .comment(" The range in blocks around the attacked mob that should be effected by the \"And His Music Was Electric\" enchantment.")
                .define("Electric Range", 5);
        ELECTRIC_DAMAGE_RETURNS = BUILDER
                .comment(" The rate of diminishing returns on each mob hit, as a percentage of the original. Uses equation\n    originalDamage * ( multiplier / entityNumber )\n where entityNumber is how many times it's jumped to a new entity")
                .define("Electric Damage Multiplier", 0.75F);
        ELECTRIC_JUMPTIME = BUILDER
                .comment(" How many ticks before the lightning jumps to the next entity.")
                .defineInRange("Electric Jump Time", 4, 0, Integer.MAX_VALUE);
        ELECTRIC_MAX_JUMPS = BUILDER
                .comment(" How many times the lightning can jump.\n Keep in mind that, after a certain amount of jumps, it deals less than half a heart.")
                .defineInRange("Electric Jump Limit", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static class Validator {
        public static boolean isString(Object object) {
            return object instanceof String;
        }

        public static boolean isStringResource(Object object) {
            return isString(object) && ResourceLocation.isValidResourceLocation((String) object);
        }
    }
}
