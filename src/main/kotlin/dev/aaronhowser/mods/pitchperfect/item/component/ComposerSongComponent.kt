package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

class ComposerSongComponent(
    val song: Song
) {

    companion object {

        val CODEC: Codec<ComposerSongComponent> =
            Song.CODEC.xmap(::ComposerSongComponent, ComposerSongComponent::song)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSongComponent> =
            Song.STREAM_CODEC.map(::ComposerSongComponent, ComposerSongComponent::song)

    }

}