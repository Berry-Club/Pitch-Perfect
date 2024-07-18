package dev.aaronhowser.mods.pitchperfect.song.parts

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import java.util.*

class SongInProgress(
    val title: String,
    val authorUuid: UUID,
    val authorName: String
) {

    private data class DelayPitch(
        val delay: Int,
        val pitch: Int
    )

    private val instrumentCounts: MutableMap<
            DelayPitch,
            MutableMap<Holder<SoundEvent>, Int>
            > = mutableMapOf()

    fun incrementInstrument(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val delayPitch = DelayPitch(delay, pitch)
        val instrumentCount = instrumentCounts[delayPitch] ?: mutableMapOf()

        val currentCount = instrumentCount[sound] ?: 0
        instrumentCount[sound] = currentCount + 1

        instrumentCounts[delayPitch] = instrumentCount
    }

    fun decrementInstrument(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val delayPitch = DelayPitch(delay, pitch)
        val instrumentCount = instrumentCounts[delayPitch] ?: mutableMapOf()

        val currentCount = instrumentCount[sound] ?: 0
        instrumentCount[sound] = currentCount - 1

        if (instrumentCount[sound] == 0) {
            instrumentCount.remove(sound)
        } else {
            instrumentCounts[delayPitch] = instrumentCount
        }
    }

    fun toSongInfo(): SongInfo {

        val instrumentBeatMap: MutableMap<Holder<SoundEvent>, List<Beat>> = mutableMapOf()

        for ((delayPitch: DelayPitch, soundAmounts: MutableMap<Holder<SoundEvent>, Int>) in instrumentCounts) {
            val (delay, pitch) = delayPitch

            for ((sound, amount) in soundAmounts) {
                val beats = instrumentBeatMap[sound]?.toMutableList() ?: mutableListOf()

                val note = Note.getFromPitch(pitch.toFloat())
                val beat = Beat(delay, listOf(note))

                for (i in 0 until amount) {
                    beats.add(beat)
                }

                instrumentBeatMap[sound] = beats
            }
        }

    }

}