package dev.aaronhowser.mods.pitchperfect.packet.server_to_client

import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.song.parts.ComposerSong
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class SetCurrentComposerSongPacket(
    private val composerSong: ComposerSong
) : IModPacket {
    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            currentComposerSong = composerSong
        }
    }

    override fun type(): CustomPacketPayload.Type<SetCurrentComposerSongPacket> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SetCurrentComposerSongPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("set_composer_song"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetCurrentComposerSongPacket> =
            ComposerSong.STREAM_CODEC.map(::SetCurrentComposerSongPacket, SetCurrentComposerSongPacket::composerSong)

        var currentComposerSong: ComposerSong? = null
            private set
    }

}