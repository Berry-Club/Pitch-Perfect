package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.pitchperfect.song.parts.Author
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import java.util.*

data class ComposerSongComponent(
    val composerSongUuid: UUID,
    val authors: List<Author>,
    val instruments: List<Holder<SoundEvent>>
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
                    SoundEvent.CODEC.listOf()
                        .fieldOf("instruments")
                        .forGetter(ComposerSongComponent::instruments)
                ).apply(instance, ::ComposerSongComponent)
            }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSongComponent> =
            StreamCodec.composite(
                UuidComponent.UUID_STREAM_CODEC, ComposerSongComponent::composerSongUuid,
                Author.STREAM_CODEC.apply(ByteBufCodecs.list()), ComposerSongComponent::authors,
                SoundEvent.STREAM_CODEC.apply(ByteBufCodecs.list()), ComposerSongComponent::instruments,
                ::ComposerSongComponent
            )
    }

}