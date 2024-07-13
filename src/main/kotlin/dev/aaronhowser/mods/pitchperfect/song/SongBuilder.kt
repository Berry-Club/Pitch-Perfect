package dev.aaronhowser.mods.pitchperfect.song

import net.minecraft.sounds.SoundEvent
import java.nio.file.Path

class SongBuilder(
    private val startingTick: Long
) {

    private val notesMap: MutableMap<SoundEvent, Map<
            Int,            // Tick
            List<Float>>    // Pitches
            > = mutableMapOf()

    fun addNote(
        currentTick: Long,
        sound: SoundEvent,
        pitch: Float
    ) {
        val ticksSinceStart = (currentTick - startingTick).toInt()

        val previousInstrumentBeats = notesMap[sound] ?: emptyMap()
        val previousBeatsThisTick = previousInstrumentBeats[ticksSinceStart] ?: emptyList()
        val withThisPitch = previousBeatsThisTick + pitch
        val newInstrumentBeats = previousInstrumentBeats + (ticksSinceStart to withThisPitch)

        notesMap[sound] = newInstrumentBeats
    }

    fun build(path: Path? = null): SongSerializer.Song {
        val instrumentBeatMap: MutableMap<SongSerializer.Instrument, List<SongSerializer.Beat>> = mutableMapOf()

        for ((soundEvent, map) in notesMap) {

            val instrument = SongSerializer.Instrument.fromSoundEvent(soundEvent)

            val instrumentBeats = mutableListOf<SongSerializer.Beat>()

            for ((tick, pitches) in map) {
                val notes: MutableList<SongSerializer.Note> = mutableListOf()
                for (pitch in pitches) {
                    val note = SongSerializer.Note.getFromPitch(pitch)

                    notes.add(note)
                }
                val beat = SongSerializer.Beat(tick, notes)
                instrumentBeats.add(beat)
            }

            instrumentBeatMap[instrument] = instrumentBeats
        }

        val song = SongSerializer.Song(instrumentBeatMap)

        if (path != null) {
            song.saveToPath(path)
        }

        return song
    }

}