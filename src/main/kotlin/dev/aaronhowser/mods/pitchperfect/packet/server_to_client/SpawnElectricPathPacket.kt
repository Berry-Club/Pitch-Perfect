package dev.aaronhowser.mods.pitchperfect.packet.server_to_client

import dev.aaronhowser.mods.pitchperfect.config.ClientConfig
import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.handling.IPayloadContext

class SpawnElectricPathPacket(
    val x1: Double,
    val y1: Double,
    val z1: Double,
    val x2: Double,
    val y2: Double,
    val z2: Double
) : IModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        TODO("Not yet implemented")
    }

    private fun spawnParticlePath() {

        val origin = Vec3(x1, y1, z1)
        val destination = Vec3(x2, y2, z2)

        val isWave = ClientConfig.ELECTRIC_PARTICLE_IS_WAVE.isTrue

    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SpawnElectricPathPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("spawn_electric_path"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SpawnElectricPathPacket> =
            StreamCodec.composite(
                ByteBufCodecs.DOUBLE, SpawnElectricPathPacket::x1,
                ByteBufCodecs.DOUBLE, SpawnElectricPathPacket::y1,
                ByteBufCodecs.DOUBLE, SpawnElectricPathPacket::z1,
                ByteBufCodecs.DOUBLE, SpawnElectricPathPacket::x2,
                ByteBufCodecs.DOUBLE, SpawnElectricPathPacket::y2,
                ByteBufCodecs.DOUBLE, SpawnElectricPathPacket::z2,
                ::SpawnElectricPathPacket
            )
    }

}