package com.aaronhowser1.pitchperfect.songs

import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.item.ModItems
import com.aaronhowser1.pitchperfect.utils.CommonUtils.asInstrument
import net.minecraft.world.entity.LivingEntity

object SongRegistry {

    private val SONGS: MutableSet<NoteSequence> = mutableSetOf()

    fun getSong(name: String): NoteSequence? {
        return SONGS.find { it.name == name }
    }

    val songsPlaying: MutableMap<LivingEntity, NoteSequence> = mutableMapOf()

    val testSong: NoteSequence = song("test song", ModItems.BIT.asInstrument()!!) {
        repeat(100) {
            beat {
                val noteValue1 = 0.5f + (it * 0.1f) % 1.5f
                val noteValue2 = 0.5f + (it * 0.3f) % 1.5f

                notes = mutableListOf(noteValue1, noteValue2)
                ticksUntilNextBeat = 3
            }
        }
    }

    val megalovania: NoteSequence = song("megalovania", ModItems.BIT.asInstrument()!!) {

    }

    private fun song(name: String, instrument: InstrumentItem, block: SongBuilder.() -> Unit): NoteSequence {
        val noteSequence = SongBuilder(name, instrument)
            .apply(block)
            .build()

        SONGS.add(noteSequence)

        return noteSequence
    }

    class SongBuilder(name: String, val instrument: InstrumentItem) {

        private val noteSequence = NoteSequence(name, instrument)

        fun beat(block: BeatBuilder.() -> Unit) {
            noteSequence.beats.add(BeatBuilder().apply(block).build())
        }

        fun build(): NoteSequence {
            return noteSequence
        }

        class BeatBuilder {

            var notes: List<Float> = listOf()
            var ticksUntilNextBeat: Int = 1

            fun build(): NoteSequence.Beat {
                return NoteSequence.Beat(notes, ticksUntilNextBeat)
            }
        }

    }

}