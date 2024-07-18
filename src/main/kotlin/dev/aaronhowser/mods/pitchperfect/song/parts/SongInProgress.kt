package dev.aaronhowser.mods.pitchperfect.song.parts

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import java.util.*

class SongInProgress(
    private val title: String,
    private val authorUuid: UUID,
    private val authorName: String
) {

    private data class Coordinate(
        val delay: Int,
        val pitch: Int
    )

    private data class SoundCount(
        val sound: Holder<SoundEvent>,
        var count: Int
    )

    private object SoundCounts {

        private val instrumentCounts: MutableMap<
                Coordinate,
                MutableSet<SoundCount>
                > = mutableMapOf()

        fun getSoundCounts(coordinate: Coordinate): MutableSet<SoundCount> {
            return instrumentCounts[coordinate] ?: mutableSetOf()
        }

        fun getSoundCount(coordinate: Coordinate, sound: Holder<SoundEvent>): SoundCount {
            return getSoundCounts(coordinate).find { it.sound.value() == sound.value() } ?: SoundCount(sound, 0)
        }

        fun getAllSoundCounts(): Map<Coordinate, MutableSet<SoundCount>> {
            return instrumentCounts
        }

    }

    fun incrementInstrument(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val coordinate = Coordinate(delay, pitch)
        val soundCount = SoundCounts.getSoundCount(coordinate, sound)

        soundCount.count++



    }

    fun decrementInstrument(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val coordinate = Coordinate(delay, pitch)
        val instrumentCount = instrumentCounts[coordinate] ?: return

        val currentCount = instrumentCount[sound] ?: return

        if (currentCount == 1) {
            instrumentCount.remove(sound)
        } else {
            instrumentCount[sound] = currentCount - 1
        }

        instrumentCounts[coordinate] = instrumentCount

        //debug print all
        for ((coord, map) in instrumentCounts) {
            println("($coord)")
            for ((soundDebug, count) in map) {
                println("   ${soundDebug.value().location} -> $count")
            }
        }

        toSongInfo()
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