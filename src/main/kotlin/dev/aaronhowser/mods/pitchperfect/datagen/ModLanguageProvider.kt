package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(output: PackOutput) : LanguageProvider(output, PitchPerfect.ID, "en_us") {

    object Item {
        const val BASS = "item.pitchperfect.bass"
        const val BASS_DRUM = "item.pitchperfect.bass_drum"
        const val BANJO = "item.pitchperfect.banjo"
        const val BIT = "item.pitchperfect.bit"
        const val CHIMES = "item.pitchperfect.chimes"
        const val COW_BELL = "item.pitchperfect.cow_bell"
        const val DIDGERIDOO = "item.pitchperfect.didgeridoo"
        const val ELECTRIC_PIANO = "item.pitchperfect.electric_piano"
        const val FLUTE = "item.pitchperfect.flute"
        const val GLOCKENSPIEL = "item.pitchperfect.glockenspiel"
        const val GUITAR = "item.pitchperfect.guitar"
        const val HARP = "item.pitchperfect.harp"
        const val SNARE_DRUM = "item.pitchperfect.snare_drum"
        const val STICKS = "item.pitchperfect.sticks"
        const val VIBRAPHONE = "item.pitchperfect.vibraphone"
        const val XYLOPHONE = "item.pitchperfect.xylophone"

        const val SHEET_MUSIC = "item.pitchperfect.sheet_music"
    }

    object Enchantment {
        const val HEALING_BEAT = "enchantment.pitchperfect.healing_beat"
        const val HEALING_BEAT_DESC = "enchantment.pitchperfect.healing_beat.desc"
        const val BWAAAP = "enchantment.pitchperfect.bwaaap"
        const val BWAAAP_DESC = "enchantment.pitchperfect.bwaaap.desc"
        const val AND_HIS_MUSIC_WAS_ELECTRIC = "enchantment.pitchperfect.and_his_music_was_electric"
        const val AND_HIS_MUSIC_WAS_ELECTRIC_DESC = "enchantment.pitchperfect.and_his_music_was_electric.desc"
    }

    object Misc {
        const val CREATIVE_TAB = "itemGroup.pitchperfect"

        const val INSTRUMENT_BROKEN = "subtitle.pitchperfect.instrument_broken"
    }

    companion object {
        fun String.toComponent(vararg args: Any): MutableComponent = Component.translatable(this, *args)
    }

    override fun addTranslations() {

        add(Item.BASS, "Bass")
        add(Item.BASS_DRUM, "Bass Drum")
        add(Item.BANJO, "Banjo")
        add(Item.BIT, "Bit Player")
        add(Item.CHIMES, "Chimes")
        add(Item.COW_BELL, "Cow Bell")
        add(Item.DIDGERIDOO, "Didgeridoo")
        add(Item.ELECTRIC_PIANO, "Electric Piano")
        add(Item.FLUTE, "Flute")
        add(Item.GLOCKENSPIEL, "Glockenspiel")
        add(Item.GUITAR, "Guitar")
        add(Item.HARP, "Harp")
        add(Item.SNARE_DRUM, "Snare Drum")
        add(Item.STICKS, "Drum Sticks")
        add(Item.VIBRAPHONE, "Vibraphone")
        add(Item.XYLOPHONE, "Xylophone")
        add(Item.SHEET_MUSIC, "Sheet Music")

        add(Enchantment.HEALING_BEAT, "Healing Beat")
        add(Enchantment.HEALING_BEAT_DESC, "Heals nearby mobs when used")
        add(Enchantment.BWAAAP, "BWAAAP")
        add(Enchantment.BWAAAP_DESC, "Knocks back nearby mobs when used")
        add(Enchantment.AND_HIS_MUSIC_WAS_ELECTRIC, "And His Music Was Electric")
        add(
            Enchantment.AND_HIS_MUSIC_WAS_ELECTRIC_DESC,
            "When anywhere in your inventory,\ndealing damage will chain to other mobs\nNOTE: Damages the item"
        )

        add(Misc.CREATIVE_TAB, "Pitch Perfect")

        add(Misc.INSTRUMENT_BROKEN, "Your instrument has broken!")
    }
}