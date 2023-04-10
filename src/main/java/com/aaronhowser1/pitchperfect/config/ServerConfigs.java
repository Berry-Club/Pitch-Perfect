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

    static {
        BUILDER.push("Server configs for Pitch Perfect");

        HEALING_BEAT_WHITELIST = BUILDER
                .comment(" Mobs that Healing Beat can always heal\n Example: [\"minecraft:piglin\"]")
                .defineListAllowEmpty(List.of("Healing Beat Whitelist"), List::of, Validator::isStringResource);

        HEALING_BEAT_BLACKLIST = BUILDER
                .comment(" Mobs that Healing Beat can never heal\n Example: [\"minecraft:cow\"]")
                .defineListAllowEmpty(List.of("Healing Beat Blacklist"), List::of, Validator::isStringResource);



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
