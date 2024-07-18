package dev.aaronhowser.mods.pitchperfect.song.parts

import net.minecraft.sounds.SoundEvent
import java.util.*

class SongInProgress(
    val authorUuid: UUID,
    val authorName: String
) {

    private data class DelayPitch(
        val delay: Int,
        val pitch: Int
    )

    private val instrumentCounts: MutableMap<
            DelayPitch,
            MutableMap<SoundEvent, Int>
            > = mutableMapOf()

    fun incrementInstrument(
        delay: Int,
        pitch: Int,
        sound: SoundEvent
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
        sound: SoundEvent
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

}