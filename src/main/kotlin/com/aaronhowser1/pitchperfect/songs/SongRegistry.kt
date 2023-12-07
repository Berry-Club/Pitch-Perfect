package com.aaronhowser1.pitchperfect.songs

import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.item.ModItems
import com.aaronhowser1.pitchperfect.utils.CommonUtils.asInstrument
import net.minecraft.world.entity.LivingEntity

object SongRegistry {

    private val SONGS: MutableSet<NoteSequence> = mutableSetOf()

    val songsPlaying: MutableMap<LivingEntity, NoteSequence> = mutableMapOf()

    val testSong: NoteSequence = song(ModItems.BIT.asInstrument()!!) {
        repeat(100) {
            beat {
                val noteValue1 = 0.5f + (it * 0.1f) % 1.5f
                val noteValue2 = 0.7f + (it * 0.1f) % 1.3f
                note(noteValue1)
                note(noteValue2)
                ticksUntilNextBeat(3)
            }
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