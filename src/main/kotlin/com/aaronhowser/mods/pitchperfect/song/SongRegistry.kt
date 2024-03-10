package com.aaronhowser.mods.pitchperfect.song

import com.aaronhowser.mods.pitchperfect.item.InstrumentItem
import com.aaronhowser.mods.pitchperfect.song.songs.Megalovania
import com.aaronhowser.mods.pitchperfect.song.songs.TestSong
import com.aaronhowser.mods.pitchperfect.utils.CommonUtils.logIfError
import net.minecraft.world.entity.LivingEntity
import kotlin.math.pow

object SongRegistry {

    private val SONGS: MutableSet<NoteSequence> = mutableSetOf()

    fun getSong(name: String): NoteSequence? {
        return SONGS.find { it.name == name }
    }

    val songsPlaying: MutableMap<LivingEntity, NoteSequence> = mutableMapOf()


    //TODO: Move to jsons in datapacks
    val testSong: NoteSequence = TestSong.register()
    val megalovania: NoteSequence = Megalovania.register()

    fun song(name: String, instrument: InstrumentItem, block: SongBuilder.() -> Unit): NoteSequence {
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

            logIfError(noteSequence.beats.isNotEmpty()) { "Song $name has no beats!" }
            logIfError(noteSequence.beats.all { it.notes.isNotEmpty() }) { "One of $name's beats has no notes!" }

            noteSequence.beats.forEachIndexed { index, beat ->
                logIfError(beat.notes.all { it in 0.5f..2f }) {
                    "One of $name's beats has a note that is not between 0.5 and 2! (Beat $index ${beat.notes})"
                }
            }

            return noteSequence
        }

        class BeatBuilder(name: String) {

            private var notes: List<Float> = listOf()
            var repeaterTicksAfter: Int = 1

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
                return NoteSequence.Beat(notes, repeaterTicksAfter * 2)
            }
        }

    }

}