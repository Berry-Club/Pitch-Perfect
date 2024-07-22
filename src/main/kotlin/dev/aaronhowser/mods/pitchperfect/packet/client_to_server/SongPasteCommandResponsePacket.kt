package dev.aaronhowser.mods.pitchperfect.packet.client_to_server

import dev.aaronhowser.mods.pitchperfect.command.PasteSongCommand
import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class SongPasteCommandResponsePacket(
    val title: String,
    val clipboard: String
) : IModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            PasteSongCommand.receivePacket(this, context)
        }
    }

    override fun type(): CustomPacketPayload.Type<SongPasteCommandResponsePacket> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SongPasteCommandResponsePacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("song_paste_response"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SongPasteCommandResponsePacket> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SongPasteCommandResponsePacket::title,
            ByteBufCodecs.STRING_UTF8, SongPasteCommandResponsePacket::clipboard,
            ::SongPasteCommandResponsePacket
        )
    }

}