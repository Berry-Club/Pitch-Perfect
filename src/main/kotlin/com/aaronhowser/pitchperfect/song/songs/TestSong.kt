package com.aaronhowser.pitchperfect.song.songs

import com.aaronhowser.pitchperfect.item.ModItems
import com.aaronhowser.pitchperfect.song.NoteSequence
import com.aaronhowser.pitchperfect.song.SongRegistry
import com.aaronhowser.pitchperfect.utils.CommonUtils.asInstrument

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