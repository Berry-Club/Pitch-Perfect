package dev.aaronhowser.mods.pitchperfect.packet.server_to_client

import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class SpawnNotePacket(
    val soundResourceLocation: ResourceLocation,
    val pitch: Float,
    val x: Double,
    val y: Double,
    val z: Double,
    val hasBwaaap: Boolean
) : IModPacket {
    override fun receiveMessage(context: IPayloadContext) {
        TODO("Not yet implemented")
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        TODO("Not yet implemented")
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SpawnNotePacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("spawn_note"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SpawnNotePacket> =
            StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, SpawnNotePacket::soundResourceLocation,
                ByteBufCodecs.FLOAT, SpawnNotePacket::pitch,
                ByteBufCodecs.DOUBLE, SpawnNotePacket::x,
                ByteBufCodecs.DOUBLE, SpawnNotePacket::y,
                ByteBufCodecs.DOUBLE, SpawnNotePacket::z,
                ByteBufCodecs.BOOL, SpawnNotePacket::hasBwaaap,
                ::SpawnNotePacket
            )
    }

}