package com.aaronhowser1.pitchperfect.config

import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.ForgeConfigSpec

object ServerConfig {

    val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    // Lifted largely from Eccentric Tome, as that was the first config I found that had lists

    // Lifted largely from Eccentric Tome, as that was the first config I found that had lists
    val HEALING_BEAT_WHITELIST: ForgeConfigSpec.ConfigValue<List<String>>
    val HEALING_BEAT_BLACKLIST: ForgeConfigSpec.ConfigValue<List<String>>

    val HEAL_AMOUNT: ForgeConfigSpec.ConfigValue<Float>
    val HEAL_COOLDOWN_MULT: ForgeConfigSpec.ConfigValue<Float>

    val BWAAAP_RANGE: ForgeConfigSpec.ConfigValue<Int>
    val BWAAAP_STRENGTH: ForgeConfigSpec.ConfigValue<Float>
    val BWAAAP_COOLDOWN_MULT: ForgeConfigSpec.ConfigValue<Int>

    val ELECTRIC_RANGE: ForgeConfigSpec.ConfigValue<Int>
    val ELECTRIC_DAMAGE_FACTOR: ForgeConfigSpec.ConfigValue<Float>
    val ELECTRIC_JUMPTIME: ForgeConfigSpec.ConfigValue<Int>
    val ELECTRIC_MAX_JUMPS: ForgeConfigSpec.ConfigValue<Int>

    init {
        BUILDER.push("Server configs for Pitch Perfect")

        HEAL_AMOUNT = BUILDER
            .comment(" How much health is healed each time Healing Beat is used.")
            .define("Healing Beat Amount", 0.25F)
        HEAL_COOLDOWN_MULT = BUILDER
            .comment(" How many ticks to cool down for every mob healed.\n Example: healing 2 mobs has a default cooldown of 3 ticks.")
            .define("Healing Cooldown Multiplier", 1.5F)

        HEALING_BEAT_WHITELIST = BUILDER
            .comment(" Mobs that Healing Beat can always heal\n Example: [\"minecraft:piglin\"]")
            .defineListAllowEmpty(
                listOf("Healing Beat Whitelist"),
                { listOf("minecraft:piglin") },
                { ResourceLocation.isValidResourceLocation(it as? String ?: "") }
            )

        HEALING_BEAT_BLACKLIST = BUILDER
            .comment(" Mobs that Healing Beat can never heal\n Example: [\"minecraft:cow\"]")
            .defineListAllowEmpty(
                listOf("Healing Beat Blacklist"),
                { listOf("minecraft:cow") },
                { ResourceLocation.isValidResourceLocation(it as? String ?: "") }
            )

        BWAAAP_RANGE = BUILDER
            .comment(" The reach the BWAAAP enchantment has.")
            .define("BWAAAP Range", 5)
        BWAAAP_STRENGTH = BUILDER
            .comment(" The strength the BWAAAP enchantment has. Decreases with distance.\n Uses the equation:\n targetDistancePercentageToRange*strength")
            .define("BWAAAP Strength", 1.25F)
        BWAAAP_COOLDOWN_MULT = BUILDER
            .comment(" How many ticks to cool down for every mob BWAAAP'd.")
            .define("BWAAAP Cooldown Multiplier", 10)

        ELECTRIC_RANGE = BUILDER
            .comment(" The range in blocks around the attacked mob that should be effected by the \"And His Music Was Electric\" enchantment.")
            .define("Electric Range", 5)
        ELECTRIC_DAMAGE_FACTOR = BUILDER
            .comment(" The rate at which the damage decreases with each jump.\n Uses the equation:\n  damage = originalDamage * (damageFactor ^ jumpNumber)")
            .define("Electric Damage Multiplier", 0.75F)
        ELECTRIC_JUMPTIME = BUILDER
            .comment(" How many ticks before the lightning jumps to the next entity.")
            .defineInRange("Electric Jump Time", 4, 0, Integer.MAX_VALUE)
        ELECTRIC_MAX_JUMPS = BUILDER
            .comment(" How many times the lightning can jump.\n Keep in mind that, after a certain amount of jumps, it deals less than half a heart.")
            .defineInRange("Electric Jump Limit", Integer.MAX_VALUE, 0, Integer.MAX_VALUE)


        BUILDER.pop()
        SPEC = BUILDER.build()
    }

}