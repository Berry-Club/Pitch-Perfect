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

    object Block {
        const val COMPOSER = "block.pitchperfect.composer"
        const val CONDUCTOR = "block.pitchperfect.conductor"
    }

    object Enchantment {
        const val HEALING_BEAT = "enchantment.pitchperfect.healing_beat"
        const val HEALING_BEAT_DESC = "enchantment.pitchperfect.healing_beat.desc"
        const val BWAAAP = "enchantment.pitchperfect.bwaaap"
        const val BWAAAP_DESC = "enchantment.pitchperfect.bwaaap.desc"
        const val AND_HIS_MUSIC_WAS_ELECTRIC = "enchantment.pitchperfect.and_his_music_was_electric"
        const val AND_HIS_MUSIC_WAS_ELECTRIC_DESC = "enchantment.pitchperfect.and_his_music_was_electric.desc"
    }

    object Message {
        const val INSTRUMENT_BROKEN = "subtitle.pitchperfect.instrument_broken"
        const val SONGS_LIST = "message.pitchperfect.songs_list"
        const val SONG_PASTE_ADDED = "message.pitchperfect.song_paste_added"
        const val CLICK_COPY_SONG_UUID = "message.pitchperfect.click_copy_song_uuid"
        const val CLICK_COPY_RAW_SONG = "message.pitchperfect.click_copy_raw_song"
        const val CLICK_PLAY_SONG = "message.pitchperfect.song_play"
        const val SONG_PASTE_FAIL_DUPLICATE = "message.pitchperfect.song_paste_fail_duplicate"
        const val SONG_PASTE_FAIL_TO_PARSE = "message.pitchperfect.song_paste_fail_to_parse"
        const val SONG_RAW_FAIL_TO_PARSE = "message.pitchperfect.song_raw_fail_to_parse"
        const val SONG_REMOVED = "message.pitchperfect.song_removed"
        const val SHEET_MUSIC_FAIL_DUPLICATE = "message.pitchperfect.sheet_music_fail_duplicate"
    }

    object Misc {
        const val CREATIVE_TAB = "itemGroup.pitchperfect"
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

        add(Block.COMPOSER, "Composer")
        add(Block.CONDUCTOR, "Conductor")

        add(Enchantment.HEALING_BEAT, "Healing Beat")
        add(Enchantment.HEALING_BEAT_DESC, "Heals nearby mobs when used")
        add(Enchantment.BWAAAP, "BWAAAP")
        add(Enchantment.BWAAAP_DESC, "Knocks back nearby mobs when used")
        add(Enchantment.AND_HIS_MUSIC_WAS_ELECTRIC, "And His Music Was Electric")
        add(
            Enchantment.AND_HIS_MUSIC_WAS_ELECTRIC_DESC,
            "When anywhere in your inventory,\ndealing damage will chain to other mobs\nNOTE: Damages the item"
        )

        add(Message.INSTRUMENT_BROKEN, "Your instrument has broken!")
        add(Message.SONGS_LIST, "Songs:")
        add(Message.SONG_PASTE_ADDED, "Song added: %s")
        add(Message.CLICK_COPY_SONG_UUID, "Click to copy UUID:\n%s")
        add(Message.CLICK_COPY_RAW_SONG, "Click to copy raw song data:\n%s")
        add(Message.CLICK_PLAY_SONG, "Click to play song.")
        add(Message.SONG_PASTE_FAIL_DUPLICATE, "Failed to add song, as an identical song already exists!\n")
        add(Message.SONG_PASTE_FAIL_TO_PARSE, "Failed to parse song from clipboard:\n%s")
        add(Message.SONG_RAW_FAIL_TO_PARSE, "Failed to parse song:\n%s")
        add(Message.SONG_REMOVED, "Song removed: %s by %s")
        add(Message.SHEET_MUSIC_FAIL_DUPLICATE, "Failed to save song, as it is a duplicate of an existing song:%s")

        add(Misc.CREATIVE_TAB, "Pitch Perfect")
    }
}