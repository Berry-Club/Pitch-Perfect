package dev.aaronhowser.mods.pitchperfect.song.songs

import dev.aaronhowser.mods.pitchperfect.item.ModItems
import dev.aaronhowser.mods.pitchperfect.song.NoteSequence
import dev.aaronhowser.mods.pitchperfect.song.SongRegistry
import dev.aaronhowser.mods.pitchperfect.utils.CommonUtils.asInstrument

object TestSong {

    fun register(): NoteSequence = SongRegistry.song("test song", ModItems.BIT.asInstrument()!!) {
        repeat(100) {
            beat {
                val noteValue1 = 0.5f + (it * 0.1f) % 1.5f
                val noteValue2 = 0.5f + (it * 0.3f) % 1.5f

                notes(noteValue1, noteValue2)
                repeaterTicksAfter = 3
            }
        }
    }

}