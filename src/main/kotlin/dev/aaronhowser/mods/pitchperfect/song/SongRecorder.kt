package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.song.parts.Beat
import dev.aaronhowser.mods.pitchperfect.song.parts.Note
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import java.nio.file.Path
import java.util.*

//TODO: Move this to SongInProgress
class SongRecorder(
    private val startingTick: Long
) {

    private val notesMap: MutableMap<Holder<SoundEvent>, Map<
            Int,            // Tick
            List<Float>>    // Pitches
            > = mutableMapOf()

    fun addNote(
        currentTick: Long,
        sound: Holder<SoundEvent>,
        pitch: Float
    ) {
        val ticksSinceStart = (currentTick - startingTick).toInt()

        val previousInstrumentBeats = notesMap[sound] ?: emptyMap()
        val previousBeatsThisTick = previousInstrumentBeats[ticksSinceStart] ?: emptyList()
        val withThisPitch = previousBeatsThisTick + pitch
        val newInstrumentBeats = previousInstrumentBeats + (ticksSinceStart to withThisPitch)

        notesMap[sound] = newInstrumentBeats
    }

    fun build(path: Path? = null): Song {
        val instrumentBeatMap: HashMap<Holder<SoundEvent>, List<Beat>> = HashMap()

        for ((soundEvent, map) in notesMap) {

            val instrumentBeats = mutableListOf<Beat>()

            for ((tick, pitches) in map) {
                val notes: MutableList<Note> = mutableListOf()
                for (pitch in pitches) {
                    val note = Note.getFromPitch(pitch)

                    notes.add(note)
                }
                val beat = Beat(tick, notes)
                instrumentBeats.add(beat)
            }

            instrumentBeatMap[soundEvent] = instrumentBeats
        }

        val song = Song(instrumentBeatMap)

        if (path != null) {
            song.saveToPath(path)
        }

        return song
    }

}