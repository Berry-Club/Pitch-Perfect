package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.song.parts.ComposerSong
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

data class ComposerSongComponent(
    val composerSong: ComposerSong
) {

    companion object {
        val CODEC: Codec<ComposerSongComponent> =
            ComposerSong.CODEC.xmap(::ComposerSongComponent, ComposerSongComponent::composerSong)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComposerSongComponent> =
            ComposerSong.STREAM_CODEC.map(::ComposerSongComponent, ComposerSongComponent::composerSong)
    }

}