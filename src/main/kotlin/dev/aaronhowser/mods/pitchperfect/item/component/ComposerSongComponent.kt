package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.screen.composer.parts.ScreenInstrument
import dev.aaronhowser.mods.pitchperfect.song.parts.Author
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class ComposerSongComponent(
    val composerSongUuid: UUID,
    val authors: List<Author>,
    val instruments: List<ScreenInstrument>
) {

    companion object {
        val CODEC: Codec<ComposerSongComponent> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    UuidComponent.UUID_CODEC
                        .fieldOf("composer_song_uuid")
                        .forGetter(ComposerSongComponent::composerSongUuid),
                    Author.CODEC.listOf()
                        .fieldOf("authors")
                        .forGetter(ComposerSongComponent::authors),
                    ScreenInstrument.CODEC.listOf()
                        .fieldOf("instruments")
                        .forGetter(ComposerSongComponent::instruments)
                ).apply(instance, ::ComposerSongComponent)
            }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSongComponent> =
            StreamCodec.composite(
                UuidComponent.UUID_STREAM_CODEC, ComposerSongComponent::composerSongUuid,
                Author.STREAM_CODEC.apply(ByteBufCodecs.list()), ComposerSongComponent::authors,
                ScreenInstrument.STREAM_CODEC.apply(ByteBufCodecs.list()), ComposerSongComponent::instruments,
                ::ComposerSongComponent
            )
    }

}