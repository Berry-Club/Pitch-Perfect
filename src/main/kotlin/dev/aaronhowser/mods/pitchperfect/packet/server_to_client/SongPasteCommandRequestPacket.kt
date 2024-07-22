package dev.aaronhowser.mods.pitchperfect.packet.server_to_client

import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.client_to_server.SongPasteCommandResponsePacket
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class SongPasteCommandRequestPacket(
    val title: String
) : IModPacket {
    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val clipboard = Minecraft.getInstance().keyboardHandler.clipboard

            ModPacketHandler.messageServer(
                SongPasteCommandResponsePacket(title, clipboard)
            )
        }
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SongPasteCommandRequestPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("song_paste_request"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SongPasteCommandRequestPacket> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, SongPasteCommandRequestPacket::title,
                ::SongPasteCommandRequestPacket
            )
    }

}