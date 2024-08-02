package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(output: PackOutput) : LanguageProvider(output, PitchPerfect.ID, "en_us") {

    object FontIcon {
        val FONT = OtherUtil.modResource("icons")
        private val STYLE = Style.EMPTY.withFont(FONT).withColor(ChatFormatting.WHITE)

        fun getIcon(instrumentItem: InstrumentItem): MutableComponent {
            val fontString = instrumentItem.fontString
            return Component.literal(fontString).setStyle(STYLE)
        }
    }

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
        const val SONG_COPIED = "message.pitchperfect.song_copied"
        const val SONG_PASTED = "message.pitchperfect.song_pasted"
        const val SONG_PASTE_FAIL_DUPLICATE = "message.pitchperfect.song_paste_fail_duplicate"
        const val SONG_PASTE_FAIL_TO_PARSE = "message.pitchperfect.song_paste_fail_to_parse"
        const val SONG_RAW_FAIL_TO_PARSE = "message.pitchperfect.song_raw_fail_to_parse"
        const val SONG_REMOVED = "message.pitchperfect.song_removed"
        const val SHEET_MUSIC_FAIL_DUPLICATE = "message.pitchperfect.sheet_music_fail_duplicate"
    }

    object Tooltip {
        const val JUMP_TO_BEAT = "tooltip.pitchperfect.jump_to_beat"
        const val JUMP_TO_BEAT_SPECIFIC = "tooltip.pitchperfect.jump_to_beat_specific"

        const val COPY = "tooltip.pitchperfect.copy"
        const val PASTE = "tooltip.pitchperfect.paste"
        const val PLAY = "tooltip.pitchperfect.play"
        const val STOP = "tooltip.pitchperfect.stop"

        const val DELAY = "tooltip.pitchperfect.delay"
        const val PITCH = "tooltip.pitchperfect.pitch"
        const val SOUNDS_LIST_START = "tooltip.pitchperfect.sounds_list_start"

    }

    object Advancement {
        const val ROOT_DESC = "advancement.pitchperfect.root.desc"

        const val HIT_MOB_TITLE = "advancement.pitchperfect.hit_mob.title"
        const val HIT_MOB_DESC = "advancement.pitchperfect.hit_mob.desc"

        const val MAKE_COMPOSER_TITLE = "advancement.pitchperfect.make_composer.title"
        const val MAKE_COMPOSER_DESC = "advancement.pitchperfect.make_composer.desc"

        const val MAKE_CONDUCTOR_TITLE = "advancement.pitchperfect.make_conductor.title"
        const val MAKE_CONDUCTOR_DESC = "advancement.pitchperfect.make_conductor.desc"

        const val CONDUCTOR_COMPLEX_TITLE = "advancement.pitchperfect.conductor_complex.title"
        const val CONDUCTOR_COMPLEX_DESC = "advancement.pitchperfect.conductor_complex.desc"

        const val ENCHANT_INSTRUMENT_TITLE = "advancement.pitchperfect.enchant_instrument.title"
        const val ENCHANT_INSTRUMENT_DESC = "advancement.pitchperfect.enchant_instrument.desc"

        const val AND_HIS_MUSIC_TITLE = "advancement.pitchperfect.and_his_music.title"
        const val AND_HIS_MUSIC_DESC = "advancement.pitchperfect.and_his_music.desc"

        const val HEALING_BEAT_TITLE = "advancement.pitchperfect.healing_beat.title"
        const val HEALING_BEAT_DESC = "advancement.pitchperfect.healing_beat.desc"

        const val BWAAAP_TITLE = "advancement.pitchperfect.bwaaap.title"
        const val BWAAAP_DESC = "advancement.pitchperfect.bwaaap.desc"
    }

    object Misc {
        const val CREATIVE_TAB = "itemGroup.pitchperfect"

        const val SONG_INFO = "pitchperfect.song_info"
        const val SONG_AUTHORS = "pitchperfect.song_authors"
        const val SONG_UUID = "pitchperfect.song_uuid"
        const val SONG_RAW = "pitchperfect.song_raw"
        const val SONG_PLAY = "pitchperfect.song_play"
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
        add(Message.SONG_COPIED, "Song copied to clipboard!")
        add(Message.SONG_PASTED, "Song pasted from clipboard!")
        add(Message.SONG_PASTE_ADDED, "Song added: %s")
        add(Message.CLICK_COPY_SONG_UUID, "Click to copy UUID:\n%s")
        add(Message.CLICK_COPY_RAW_SONG, "Click to copy raw song data:\n%s")
        add(Message.CLICK_PLAY_SONG, "Click to play song.")
        add(Message.SONG_PASTE_FAIL_DUPLICATE, "Failed to add song, as an identical song already exists!\n")
        add(Message.SONG_PASTE_FAIL_TO_PARSE, "Failed to parse song from clipboard:\n%s")
        add(Message.SONG_RAW_FAIL_TO_PARSE, "Failed to parse song:\n%s")
        add(Message.SONG_REMOVED, "Song removed: %s by %s")
        add(Message.SHEET_MUSIC_FAIL_DUPLICATE, "Failed to save song, as it is a duplicate of an existing song:%s")

        add(
            Advancement.ROOT_DESC,
            "Get any instrument!\n\nRight-click to play a note, depending on the angle you're looking!"
        )
        add(Advancement.HIT_MOB_TITLE, "Hit the Right Note")
        add(Advancement.HIT_MOB_DESC, "Hit a mob using an instrument")
        add(Advancement.MAKE_COMPOSER_TITLE, "Maestro")
        add(
            Advancement.MAKE_COMPOSER_DESC,
            "Make a Composer\n\nThis acts as a simple digital audio workstation to make songs!"
        )
        add(Advancement.MAKE_CONDUCTOR_TITLE, "Ghostly Symphony")
        add(
            Advancement.MAKE_CONDUCTOR_DESC,
            "Make a Conductor\n\nPlace Armor Stands nearby and give them Instruments, put Sheet Music in the Conductor, then give it a redstone signal to play the song!"
        )
        add(Advancement.CONDUCTOR_COMPLEX_TITLE, "Polyphony")
        add(
            Advancement.CONDUCTOR_COMPLEX_DESC,
            "Play a song in the Conductor that has at least 3 different instruments"
        )
        add(Advancement.ENCHANT_INSTRUMENT_TITLE, "The Magic of Music")
        add(Advancement.ENCHANT_INSTRUMENT_DESC, "Enchant any instrument")
        add(Advancement.AND_HIS_MUSIC_TITLE, "And His Music Was Electric")
        add(
            Advancement.AND_HIS_MUSIC_DESC,
            "Activate the enchantment And His Music Was Electric\n\nWhen anywhere in your inventory, dealing damage will chain to other mobs, but damage the instrument"
        )
        add(Advancement.HEALING_BEAT_TITLE, "Music Therapy")
        add(Advancement.HEALING_BEAT_DESC, "Heal something using the Healing Beat enchantment")
        add(Advancement.BWAAAP_TITLE, "BWAAAAAAAP")
        add(Advancement.BWAAAP_DESC, "Knock back entities with the BWAAAP enchantment")

        add(Tooltip.JUMP_TO_BEAT, "Jump to beat")
        add(Tooltip.JUMP_TO_BEAT_SPECIFIC, "Jump to beat %s")
        add(Tooltip.COPY, "Copy")
        add(Tooltip.PASTE, "Paste")
        add(Tooltip.PLAY, "Play")
        add(Tooltip.STOP, "Stop")
        add(Tooltip.DELAY, "Delay: ")
        add(Tooltip.PITCH, "Pitch: ")
        add(Tooltip.SOUNDS_LIST_START, "Sounds:")

        add(Misc.CREATIVE_TAB, "Pitch Perfect")

        add(Misc.SONG_INFO, "%1\$s - %2\$s %3\$s %4\$s %5\$s")
        add(Misc.SONG_AUTHORS, "[Authors]")
        add(Misc.SONG_UUID, "[UUID]")
        add(Misc.SONG_RAW, "[Raw]")
        add(Misc.SONG_PLAY, "[Play]")

    }
}