package dev.aaronhowser.mods.pitchperfect.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair


class ServerConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

        val CONFIG: ServerConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var CHANCE_MOB_SPAWNS_WITH_INSTRUMENT: ModConfigSpec.DoubleValue

        lateinit var HEALING_BEAT_WHITELIST: ModConfigSpec.ConfigValue<List<String>>
        lateinit var HEALING_BEAT_BLACKLIST: ModConfigSpec.ConfigValue<List<String>>

        lateinit var HEAL_RANGE: ModConfigSpec.DoubleValue
        lateinit var HEAL_AMOUNT: ModConfigSpec.DoubleValue
        lateinit var HEAL_COOLDOWN_PER: ModConfigSpec.DoubleValue

        lateinit var BWAAAP_RANGE: ModConfigSpec.DoubleValue
        lateinit var BWAAAP_STRENGTH: ModConfigSpec.DoubleValue
        lateinit var BWAAAP_COOLDOWN_FACTOR: ModConfigSpec.DoubleValue

        lateinit var ELECTRIC_RANGE: ModConfigSpec.DoubleValue
        lateinit var ELECTRIC_DAMAGE_FACTOR: ModConfigSpec.DoubleValue
        lateinit var ELECTRIC_JUMP_TIME: ModConfigSpec.IntValue
        lateinit var ELECTRIC_MAX_JUMPS: ModConfigSpec.IntValue
    }

    init {
        ServerConfigs()
        builder.build()
    }

    private fun ServerConfigs() {

        builder.push("Server")

        CHANCE_MOB_SPAWNS_WITH_INSTRUMENT = builder
            .comment("The chance that a monster with an empty main-hand will have it filled with an instrument on spawn.")
            .defineInRange("Mob Spawns With Instrument Chance", 0.005, 0.0, 1.0)

        HEAL_RANGE = builder
            .comment("The range in blocks around the user that the Healing Beat enchantment checks for mobs to heal.")
            .defineInRange("Healing Beat Range", 5.0, 0.0, 64.0)
        HEAL_AMOUNT = builder
            .comment("How much health is healed each time Healing Beat is used.")
            .defineInRange("Healing Beat Amount", 0.25, 0.0, Double.MAX_VALUE)
        HEAL_COOLDOWN_PER = builder
            .comment("How many ticks to cool down for every mob healed.\nExample: healing 2 mobs has a default cooldown of 3 ticks.")
            .defineInRange("Healing Cooldown Multiplier", 1.5, 0.0, Double.MAX_VALUE)

        //TODO: Move this to use entity type tags
        HEALING_BEAT_WHITELIST = builder
            .comment("Mobs that Healing Beat can always heal\nExample: [\"minecraft:piglin\"]")
            .defineListAllowEmpty("Healing Beat Whitelist", listOf()) { true }

        //TODO: Move this to use entity type tags
        HEALING_BEAT_BLACKLIST = builder
            .comment("Mobs that Healing Beat can never heal\nExample: [\"minecraft:cow\"]")
            .defineListAllowEmpty("Healing Beat Blacklist", listOf()) { true }

        BWAAAP_RANGE = builder
            .comment("The reach the BWAAAP enchantment has.")
            .defineInRange("BWAAAP Range", 5.0, 0.0, 128.0)
        BWAAAP_STRENGTH = builder
            .comment("The strength the BWAAAP enchantment has. Decreases with distance.\nUses the equation:\nstrength * targetsDistanceToMaxRange")
            .defineInRange("BWAAAP Strength", 1.25, 0.0, Double.MAX_VALUE)
        BWAAAP_COOLDOWN_FACTOR = builder
            .comment("How many ticks to cool down for every mob BWAAAP'd.")
            .defineInRange("BWAAAP Cooldown Multiplier", 10.0, 0.0, Double.MAX_VALUE)

        ELECTRIC_RANGE = builder
            .comment("The range in blocks around the attacked mob that should be effected by the \"And His Music Was Electric\"enchantment.")
            .defineInRange("Electric Range", 5.0, 0.0, 128.0)
        ELECTRIC_DAMAGE_FACTOR = builder
            .comment("The rate at which the damage decreases with each jump.\nUses the equation:\ndamage = originalDamage * (damageFactor ^ jumpNumber)")
            .defineInRange("Electric Damage Multiplier", 0.75, 0.0, Double.MAX_VALUE)
        ELECTRIC_JUMP_TIME = builder
            .comment("How many ticks before the lightning jumps to the next entity.")
            .defineInRange("Electric Jump Time", 4, 0, Integer.MAX_VALUE)
        ELECTRIC_MAX_JUMPS = builder
            .comment("How many times the lightning can jump.\nIf it would do less than 0.5 damage, it will stop.")
            .defineInRange("Electric Jump Limit", Integer.MAX_VALUE, 0, Integer.MAX_VALUE)

        builder.pop()
    }

}