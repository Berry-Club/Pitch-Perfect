package com.aaronhowser1.pitchperfect.songs

import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.item.ModItems
import com.aaronhowser1.pitchperfect.utils.CommonUtils.asInstrument

object SongRegistry {

    val SONGS: MutableSet<NoteSequence> = mutableSetOf()

    val testSong: NoteSequence = song(ModItems.BIT.get().asInstrument()!!) {
        beat {
            note(0.5f)
            note(0.5f)
            note(0.5f)
            note(0.5f)

            ticksUntilNextBeat(5)
        }
    }

    private fun song(instrument: InstrumentItem, block: SongBuilder.() -> Unit): NoteSequence {
        val s = SongBuilder(instrument)
            .apply(block)
            .build()

        SONGS.add(s)

        return s
    }

    class SongBuilder(val instrument: InstrumentItem) {

        private val noteSequence = NoteSequence(instrument)

        fun beat(block: BeatBuilder.() -> Unit) {
            noteSequence.beats.add(BeatBuilder().apply(block).build())
        }

        fun build(): NoteSequence {
            return noteSequence
        }

        class BeatBuilder {

            private val notes: MutableList<Float> = mutableListOf()
            private var ticksUntilNextBeat: Int = 0

            fun note(pitch: Float) {
                notes.add(pitch)
            }

            fun ticksUntilNextBeat(ticks: Int) {
                if (ticksUntilNextBeat != 0) throw IllegalStateException("Ticks until next beat already set")
                ticksUntilNextBeat = ticks
            }

            fun build(): NoteSequence.Beat {
                return NoteSequence.Beat(notes, ticksUntilNextBeat)
            }
        }

    }

}