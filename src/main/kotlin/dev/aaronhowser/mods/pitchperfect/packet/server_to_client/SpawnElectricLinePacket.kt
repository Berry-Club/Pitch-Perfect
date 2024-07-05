package dev.aaronhowser.mods.pitchperfect.packet.server_to_client

import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class SpawnElectricLinePacket(
    val start: DoublePos,
    val end: DoublePos,
    val int: Int
) : IModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        AndHisMusicWasElectricEnchantment.ElectricLine(
            start.x, start.y, start.z,
            end.x, end.y, end.z,
            int
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SpawnElectricLinePacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("spawn_electric_line"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SpawnElectricLinePacket> =
            StreamCodec.composite(
                DoublePos.STREAM_CODEC, SpawnElectricLinePacket::start,
                DoublePos.STREAM_CODEC, SpawnElectricLinePacket::end,
                ByteBufCodecs.INT, SpawnElectricLinePacket::int,
                ::SpawnElectricLinePacket
            )

        fun getFromDoubles(
            x1: Double,
            y1: Double,
            z1: Double,
            x2: Double,
            y2: Double,
            z2: Double,
            int: Int
        ): SpawnElectricLinePacket {
            return SpawnElectricLinePacket(
                DoublePos(x1, y1, z1),
                DoublePos(x2, y2, z2),
                int
            )
        }

    }

    data class DoublePos(
        val x: Double,
        val y: Double,
        val z: Double
    ) {
        companion object {
            val STREAM_CODEC: StreamCodec<ByteBuf, DoublePos> =
                StreamCodec.composite(
                    ByteBufCodecs.DOUBLE, DoublePos::x,
                    ByteBufCodecs.DOUBLE, DoublePos::y,
                    ByteBufCodecs.DOUBLE, DoublePos::z,
                    ::DoublePos
                )
        }
    }

}