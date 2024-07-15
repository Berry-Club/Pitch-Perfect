package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import dev.aaronhowser.mods.pitchperfect.song.parts.SongInfo
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

data class SongInfoComponent(
    val songInfo: SongInfo
) {

    companion object {
        val CODEC: Codec<SongInfoComponent> =
            SongInfo.CODEC.xmap(::SongInfoComponent, SongInfoComponent::songInfo)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SongInfoComponent> =
            SongInfo.STREAM_CODEC.map(::SongInfoComponent, SongInfoComponent::songInfo)

        val component: DataComponentType<SongInfoComponent> by lazy {
            ModDataComponents.SONG_INFO_COMPONENT.get()
        }
    }

}