package dev.aaronhowser.mods.pitchperfect.song.parts

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import java.util.*

class SongInProgress(
    private val title: String,
    private val authorUuid: UUID,
    private val authorName: String
) {

    data class Coordinate(
        val delay: Int,
        val pitch: Int
    )

    private val instrumentCounts: MutableMap<
            Coordinate,
            MutableMap<Holder<SoundEvent>, Int>
            > = mutableMapOf()

    fun incrementInstrument(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val coordinate = Coordinate(delay, pitch)
        val instrumentCount = instrumentCounts[coordinate] ?: mutableMapOf()

        val currentCount = instrumentCount[sound] ?: 0
        instrumentCount[sound] = currentCount + 1

        instrumentCounts[coordinate] = instrumentCount
    }

    fun decrementInstrument(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val coordinate = Coordinate(delay, pitch)
        val instrumentCount = instrumentCounts[coordinate] ?: mutableMapOf()

        val currentCount = instrumentCount[sound] ?: 0
        instrumentCount[sound] = currentCount - 1

        if (instrumentCount[sound] == 0) {
            instrumentCount.remove(sound)
        } else {
            instrumentCounts[coordinate] = instrumentCount
        }
    }

    fun toSongInfo(): SongInfo {

        data class MutableBeat(
            val at: Int,
            val notes: MutableList<Note>
        ) {
            fun toBeat(): Beat {
                return Beat(at, notes.toList())
            }
        }

        val instrumentMutableBeatMap: MutableMap<Holder<SoundEvent>, List<MutableBeat>> = mutableMapOf()

        for ((delayPitch, map) in instrumentCounts) {
            val (tick, pitch) = delayPitch

            val note = Note.getFromPitch(pitch)

            for ((soundEvent, count) in map) {
                val instrumentBeats = instrumentMutableBeatMap[soundEvent]?.toMutableList() ?: mutableListOf()

                val beatsThisTIck = instrumentBeats.find { it.at == tick } ?: MutableBeat(tick, mutableListOf())

                for (i in 0 until count) {
                    beatsThisTIck.notes.add(note)
                }

                if (beatsThisTIck !in instrumentBeats) {
                    instrumentBeats.add(beatsThisTIck)
                }
            }

        }

        val instrumentBeatMap: HashMap<Holder<SoundEvent>, List<Beat>> = HashMap()

        for ((soundEvent, mutableBeats) in instrumentMutableBeatMap) {
            val beats = mutableBeats.map { it.toBeat() }
            instrumentBeatMap[soundEvent] = beats
        }

        return SongInfo(
            title,
            authorUuid,
            authorName,
            Song(instrumentBeatMap)
        )
    }

}