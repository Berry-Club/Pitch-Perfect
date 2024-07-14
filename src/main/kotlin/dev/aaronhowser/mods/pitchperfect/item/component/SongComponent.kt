package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

data class SongComponent(
    val song: Song
) {

    companion object {
        val CODEC: Codec<SongComponent> =
            Song.CODEC.xmap(::SongComponent, SongComponent::song)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongComponent> =
            Song.STREAM_CODEC.map(::SongComponent, SongComponent::song)
    }

}