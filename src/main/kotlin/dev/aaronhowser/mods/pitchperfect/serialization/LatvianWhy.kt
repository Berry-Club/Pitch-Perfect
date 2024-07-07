package dev.aaronhowser.mods.pitchperfect.serialization

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import java.util.function.Function
import kotlin.math.pow

object LatvianWhy {

    data class Song(
        val beats: Map<NoteBlockInstrument, List<Beat>>
    ) {

        companion object {
            private val INSTRUMENTS = NoteBlockInstrument.entries.toTypedArray()

            val CODEC: Codec<Song> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.unboundedMap(StringRepresentable.fromEnum { INSTRUMENTS }, Beat.CODEC.listOf())
                        .fieldOf("beats")
                        .forGetter(Song::beats)
                ).apply(instance, LatvianWhy::Song)
            }

            val STREAM_CODEC: StreamCodec<ByteBuf, Song> = ByteBufCodecs.map(
                ::HashMap,
                ByteBufCodecs.idMapper({ INSTRUMENTS[it] }, NoteBlockInstrument::ordinal),
                Beat.STREAM_CODEC.apply(ByteBufCodecs.list())
            ).map(LatvianWhy::Song) { HashMap(it.beats) }

        }

    }

    data class Beat(
        val at: Int,
        val notes: List<Note>
    ) {
        companion object {

            val CODEC: Codec<Beat> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.optionalFieldOf("at", 0).forGetter(Beat::at),
                    Note.ONE_OR_MORE_CODEC.fieldOf("notes").forGetter(Beat::notes)
                ).apply(instance, LatvianWhy::Beat)
            }

            val STREAM_CODEC: StreamCodec<ByteBuf, Beat> = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Beat::at,
                Note.STREAM_CODEC.apply(ByteBufCodecs.list()), Beat::notes,
                LatvianWhy::Beat
            )

        }

    }

    enum class Note(
        private val note: Int,
        private val octave: Int
    ) : StringRepresentable {

        //        A0(9, 0),
//        A0S(10, 0),
//        B0(11, 0),
//        C1(0, 1),
//        C1S(1, 1),
//        D1(2, 1),
//        D1S(3, 1),
//        E1(4, 1),
//        F1(5, 1),
//        F1S(6, 1),
//        G1(7, 1),
//        G1S(8, 1),
//        A1(9, 1),
//        A1S(10, 1),
//        B1(11, 1),
//        C2(0, 2),
//        C2S(1, 2),
//        D2(2, 2),
//        D2S(3, 2),
//        E2(4, 2),
//        F2(5, 2),
//        F2S(6, 2),
//        G2(7, 2),
//        G2S(8, 2),
//        A2(9, 2),
//        A2S(10, 2),
//        B2(11, 2),
//        C3(0, 3),
//        C3S(1, 3),
//        D3(2, 3),
//        D3S(3, 3),
//        E3(4, 3),
        F3(5, 3),
        F3S(6, 3),
        G3(7, 3),
        G3S(8, 3),
        A3(9, 3),
        A3S(10, 3),
        B3(11, 3),
        C4(0, 4),
        C4S(1, 4),
        D4(2, 4),
        D4S(3, 4),
        E4(4, 4),
        F4(5, 4),
        F4S(6, 4),
        G4(7, 4),
        G4S(8, 4),
        A4(9, 4),
        A4S(10, 4),
        B4(11, 4),
        C5(0, 5),
        C5S(1, 5),
        D5(2, 5),
        D5S(3, 5),
        E5(4, 5),
        F5(5, 5),
//        F5S(6, 5),
//        G5(7, 5),
//        G5S(8, 5),
//        A5(9, 5),
//        A5S(10, 5),
//        B5(11, 5),
//        C6(0, 6),
//        C6S(1, 6),
//        D6(2, 6),
//        D6S(3, 6),
//        E6(4, 6),
//        F6(5, 6),
//        F6S(6, 6),
//        G6(7, 6),
//        G6S(8, 6),
//        A6(9, 6),
//        A6S(10, 6),
//        B6(11, 6),
//        C7(0, 7),
//        C7S(1, 7),
//        D7(2, 7),
//        D7S(3, 7),
//        E7(4, 7),
//        F7(5, 7),
//        F7S(6, 7),
//        G7(7, 7),
//        G7S(8, 7),
//        A7(9, 7),
//        A7S(10, 7),
//        B7(11, 7),
//        C8(0, 8),
//        C8S(1, 8),
//        D8(2, 8),
//        D8S(3, 8),
//        E8(4, 8),
//        F8(5, 8),
//        F8S(6, 8),
//        G8(7, 8),
//        G8S(8, 8),
//        A8(9, 8),
//        A8S(10, 8),
//        B8(11, 8),
//        C9(0, 9),
//        C9S(1, 9),
//        D9(2, 9),
//        D9S(3, 9),
//        E9(4, 9),
//        F9(5, 9),
//        F9S(6, 9),
//        G9(7, 9),
//        G9S(8, 9)

        ;

        private val displayName: String = when (note) {
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

        private val midiNoteNumber: Int = 21 + (octave * 12) + note
        private val frequency: Float = (440.0 * 2.0.pow(midiNoteNumber - 69.0) / 12.0).toFloat()
        private val pitch: Float = 2.0.pow((midiNoteNumber - 69.0) / 12.0).toFloat()

        override fun getSerializedName(): String {
            return this.displayName
        }

        val playSoundPitch
            get() = playSoundPitches[this]

        companion object {
            private val VALUES = entries.toTypedArray()

            val CODEC: Codec<Note> = StringRepresentable.fromEnum { VALUES }

            val STREAM_CODEC: StreamCodec<ByteBuf, Note> = ByteBufCodecs.idMapper({ VALUES[it] }, Note::ordinal)

            val ONE_OR_MORE_CODEC: Codec<List<Note>> = Codec
                .either(CODEC, CODEC.listOf())
                .xmap(
                    { either: Either<Note, MutableList<Note>> -> either.map(::listOf, Function.identity()) },
                    { list: List<Note> -> if (list.size == 1) Either.left(list.first()) else Either.right(list) }
                )

            val playSoundPitches
                get() = mapOf(
                    F3S to 0.5f,
                    G3 to 2f.pow(-11f / 12f),
                    G3S to 2f.pow(-10f / 12f),
                    A3 to 2f.pow(-9f / 12f),
                    A3S to 2f.pow(-8f / 12f),
                    B3 to 2f.pow(-7f / 12f),
                    C4 to 2f.pow(-6f / 12f),
                    C4S to 2f.pow(-5f / 12f),
                    D4 to 2f.pow(-4f / 12f),
                    D4S to 2f.pow(-3f / 12f),
                    E4 to 2f.pow(-2f / 12f),
                    F4 to 2f.pow(-1f / 12f),
                    F4S to 1f,
                    G4 to 2f.pow(1f / 12f),
                    G4S to 2f.pow(2f / 12f),
                    A4 to 2f.pow(3f / 12f),
                    A4S to 2f.pow(4f / 12f),
                    B4 to 2f.pow(5f / 12f),
                    C5 to 2f.pow(6f / 12f),
                    C5S to 2f.pow(7f / 12f),
                    D5 to 2f.pow(8f / 12f),
                    D5S to 2f.pow(9f / 12f),
                    E5 to 2f.pow(10f / 12f),
                    F5 to 2f.pow(11f / 12f)
                )
        }

    }

}