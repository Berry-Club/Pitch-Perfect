package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import kotlin.math.max


data class Beat(
	val at: Int,
	val notes: List<Note>
) {

	override fun toString(): String {
		val stringBuilder = StringBuilder()

		if (notes.size == 1) {
			stringBuilder.append(notes.first().serializedName)
		} else {
			stringBuilder.append('[')

			for (i in notes.indices) {
				if (i != 0) {
					stringBuilder.append(',')
				}

				stringBuilder.append(notes[i].serializedName)
			}

			stringBuilder.append(']')
		}

		stringBuilder.append('@')
		stringBuilder.append(at)
		return stringBuilder.toString()
	}

	companion object {

		val CODEC: Codec<Beat> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT
						.fieldOf("at")
						.forGetter(Beat::at),
					Note.CODEC.listOf()
						.fieldOf("notes")
						.forGetter(Beat::notes),
				).apply(instance, ::Beat)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, Beat> = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, Beat::at,
			Note.STREAM_CODEC.apply(ByteBufCodecs.list()), Beat::notes,
			::Beat
		)

		fun fromStringReader(reader: StringReader): Beat {
			reader.skipWhitespace()

			val notes: MutableList<Note> = mutableListOf()

			if (reader.peek() == '[') {
				reader.skip()
				reader.skipWhitespace()

				while (reader.canRead() && reader.peek() != ']') {
					notes.add(Note.parse(reader))

					while (reader.canRead() && reader.peek() == ',') {
						reader.skip()
						reader.skipWhitespace()
					}
				}

				reader.expect(']')
			} else {
				notes.add(Note.parse(reader))
			}

			reader.expect('@')
			val at: Int = reader.readInt()

			return Beat(max(0, at), notes.toList())
		}
	}
}