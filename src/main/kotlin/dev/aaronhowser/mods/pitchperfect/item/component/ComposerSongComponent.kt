package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.ScreenInstrument
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class ComposerSongComponent(
	val composerSongUuid: UUID,
	val instrumentCounts: Map<ScreenInstrument, Int>
) {

	companion object {
		val CODEC: Codec<ComposerSongComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					OtherUtil.UUID_CODEC
						.fieldOf("composer_song_uuid")
						.forGetter(ComposerSongComponent::composerSongUuid),
					Codec.unboundedMap(ScreenInstrument.CODEC, Codec.INT)
						.fieldOf("instrument_counts")
						.forGetter(ComposerSongComponent::instrumentCounts)
				).apply(instance, ::ComposerSongComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSongComponent> =
			StreamCodec.composite(
				OtherUtil.UUID_STREAM_CODEC, ComposerSongComponent::composerSongUuid,
				ByteBufCodecs.map(
					OtherUtil::weirdMapFunctionThingy,
					ScreenInstrument.STREAM_CODEC,
					ByteBufCodecs.VAR_INT
				), ComposerSongComponent::instrumentCounts,
				::ComposerSongComponent
			)
	}

}