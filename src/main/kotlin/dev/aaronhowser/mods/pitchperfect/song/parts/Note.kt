package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.math.pow


enum class Note(
    val note: Int,
    val octave: Int,
    val color: Int
) : StringRepresentable {
    F3S(6, 3, 0x77D700),
    G3(7, 3, 0x95C000),
    G3S(8, 3, 0xB2A500),
    A3(9, 3, 0xCC8600),
    A3S(10, 3, 0xE26500),
    B3(11, 3, 0xF34100),
    C4(0, 4, 0xFC1E00),
    C4S(1, 4, 0xFE000F),
    D4(2, 4, 0xF70033),
    D4S(3, 4, 0xE8005A),
    E4(4, 4, 0xCF0083),
    F4(5, 4, 0xAE00A9),
    F4S(6, 4, 0x8600CC),
    G4(7, 4, 0x5B00E7),
    G4S(8, 4, 0x2D00F9),
    A4(9, 4, 0x020AFE),
    A4S(10, 4, 0x0037F6),
    B4(11, 4, 0x0068E0),
    C5(0, 5, 0x009ABC),
    C5S(1, 5, 0x00C68D),
    D5(2, 5, 0x00E958),
    D5S(3, 5, 0x00FC21),
    E5(4, 5, 0x1FFC00),
    F5(5, 5, 0x59E800),
    F5S(6, 5, 0x94C100),
    ;

    val displayName: String = when (note) {
        0 -> "C"
        1 -> "C#"
        2 -> "D"
        3 -> "D#"
        4 -> "E"
        5 -> "F"
        6 -> "F#"
        7 -> "G"
        8 -> "G#"
        9 -> "A"
        10 -> "A#"
        11 -> "B"
        else -> "X"
    } + octave

    val midiNoteNumber: Int = 21 + (octave * 12) + note
    val frequency: Float = (440.0 * 2.0.pow((midiNoteNumber - 69.0) / 12.0)).toFloat()
    val pitch: Float = 2.0.pow((ordinal - 12.0) / 12.0).toFloat()

    override fun getSerializedName(): String {
        return displayName
    }

    override fun toString(): String {
        return displayName
    }

    companion object {
        val VALUES: Array<Note> = entries.toTypedArray()
        val MAP: Map<String, Note> = Arrays.stream(VALUES).collect(
            Collectors.toMap(
                { it.serializedName }, Function.identity()
            )
        )
        val STREAM_CODEC: StreamCodec<ByteBuf, Note> = ByteBufCodecs.idMapper(
            { b: Int -> VALUES[b] },
            { obj: Note -> obj.ordinal })

        private val INVALID_NOTE = SimpleCommandExceptionType(Component.literal("Invalid note"))
        private val INVALID_OCTAVE = SimpleCommandExceptionType(Component.literal("Invalid octave"))
        private val NOTE_NOT_FOUND = SimpleCommandExceptionType(Component.literal("Note not found"))

        @Throws(CommandSyntaxException::class)
        fun parse(reader: StringReader): Note {
            reader.skipWhitespace()

            val stringBuilder = StringBuilder(3)

            val firstChar = reader.read()

            if (firstChar <= 'A' || firstChar >= 'G') {
                throw INVALID_NOTE.createWithContext(reader)
            }

            stringBuilder.append(firstChar)

            if (reader.peek() == '#') {
                reader.skip()
                stringBuilder.append('#')
            }

            val o = reader.read()

            if (o < '3' || o > '5') {
                throw INVALID_OCTAVE.createWithContext(reader)
            }

            stringBuilder.append(o)

            val note = MAP[stringBuilder.toString()] ?: throw NOTE_NOT_FOUND.createWithContext(reader)

            return note
        }

        fun getFromPitch(pitch: Float): Note {
            val note = VALUES.firstOrNull { it.getGoodPitch() == pitch }

            if (note == null) {
                val closestNote = VALUES.minByOrNull {
                    Mth.abs(it.getGoodPitch() - pitch)
                }

                requireNotNull(closestNote) { "No note found for pitch $pitch (How did this happen????)" }

                PitchPerfect.LOGGER.error("No note with exact pitch $pitch, using closest note: $closestNote (${closestNote.getGoodPitch()}")

                return closestNote
            }

            return note
        }

    }

    fun getGoodPitch(): Float {
        // Refers to F#4, which has a Minecraft pitch of 1.0
        val referenceNote = 6
        val referenceOctave = 4

        val refNoteValue = referenceNote + (referenceOctave * 12)
        val noteValue = this.note + (this.octave * 12)
        return 2f.pow((noteValue - refNoteValue) / 12f)
    }
}