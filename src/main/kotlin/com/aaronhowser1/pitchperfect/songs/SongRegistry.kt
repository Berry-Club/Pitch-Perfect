package com.aaronhowser1.pitchperfect.songs

import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.item.ModItems
import com.aaronhowser1.pitchperfect.utils.CommonUtils.asInstrument
import com.aaronhowser1.pitchperfect.utils.CommonUtils.checkError
import net.minecraft.world.entity.LivingEntity
import kotlin.math.pow

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

                notes(noteValue1, noteValue2)
                ticksUntilNextBeat = 3
            }
        }
    }

    val megalovania: NoteSequence = song("megalovania", ModItems.BIT.asInstrument()!!) {
        beat {
            notes(8)
        }
        beat {
            notes(8)
        }
        beat {
            notes(20)
        }
        beat {
            notes(15)
        }
        beat {
            notes(14)
        }
        beat {
            notes(13)
        }
        beat {
            notes(11)
        }
        beat {
            notes(8)
        }
        beat {
            notes(11)
        }
        beat {
            notes(13)
        }
        beat {
            notes(6)
        }

    }

    private fun song(name: String, instrument: InstrumentItem, block: SongBuilder.() -> Unit): NoteSequence {
        val noteSequence = SongBuilder(name, instrument)
            .apply(block)
            .build()

        SONGS.add(noteSequence)

        return noteSequence
    }

    class SongBuilder(private val name: String, val instrument: InstrumentItem) {

        private val noteSequence = NoteSequence(name, instrument)

        fun beat(block: BeatBuilder.() -> Unit) {
            noteSequence.beats.add(BeatBuilder(name).apply(block).build())
        }

        fun build(): NoteSequence {

            checkError(noteSequence.beats.isNotEmpty()) { "Song $name has no beats!" }
            checkError(noteSequence.beats.all { it.notes.isNotEmpty() }) { "One of $name's beats has no notes!" }

            noteSequence.beats.forEachIndexed { index, beat ->
                checkError(beat.notes.all { it in 0.5f..2f }) {
                    "One of $name's beats has a note that is not between 0.5 and 2! (Beat $index ${beat.notes})"
                }
            }

            return noteSequence
        }

        class BeatBuilder(name: String) {

            private var notes: List<Float> = listOf()
            var ticksUntilNextBeat: Int = 1

            fun notes(vararg list: Float) {
                notes = list.toList()
            }

            fun notes(vararg list: Int) {
                val floatList = mutableListOf<Float>()
                for (i in list) {

                    // https://minecraft.wiki/w/Note_Block#Notes
                    // A Note Block's float pitch value is calculated as 2 ^ (note / 12)
                    // Where "note" ranges from -12 to 12
                    // We'll input it as 0-24 though, because that's easier to work with
                    val truePitch = 2f.pow((i - 12) / 12f)
                    floatList.add(truePitch)
                }

                notes(*floatList.toFloatArray())
            }

            fun build(): NoteSequence.Beat {
                return NoteSequence.Beat(notes, ticksUntilNextBeat)
            }
        }

    }

}