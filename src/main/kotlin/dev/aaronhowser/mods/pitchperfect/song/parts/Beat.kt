package dev.aaronhowser.mods.pitchperfect.song.parts

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec


data class Beat(
	val at: Int,
	val notes: List<Note>
) {

	/**
	 * - Beat(5, C4) -> "C4@5"
	 * - Beat(10, {C4,E4,G4}) -> "[C4,E4,G4]@10"
	 */
	override fun toString(): String {
		val notesString = if (notes.size == 1) {
			notes.first().serializedName
		} else {
			notes.joinToString(
				separator = ",",
				prefix = "[",
				postfix = "]",
				transform = { it.serializedName }
			)
		}

		return "$notesString@$at"
	}

	companion object {

		val CODEC: Codec<Beat> =
			Codec.STRING.xmap(
				::fromString,
				Beat::toString
			)

		private fun fromString(string: String): Beat {
			val atIndex = string.lastIndexOf('@')
			require(atIndex != -1) { "Invalid beat string: $string" }

			val notePart = string.take(atIndex)
			val at = string.substring(atIndex + 1).toIntOrNull()
			require(at != null && at >= 0) { "Invalid beat time: ${string.substring(atIndex + 1)}" }

			val notes = if (notePart.startsWith('[') && notePart.endsWith(']')) {
				val noteStrings = notePart
					.drop(1)
					.dropLast(1)
					.split(',')

				noteStrings.map(Note::fromString)
			} else {
				listOf(Note.fromString(notePart))
			}

			return Beat(at, notes)
		}

		val STREAM_CODEC: StreamCodec<ByteBuf, Beat> = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, Beat::at,
			Note.STREAM_CODEC.apply(ByteBufCodecs.list()), Beat::notes,
			::Beat
		)
	}
}