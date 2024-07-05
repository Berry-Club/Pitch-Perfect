package dev.aaronhowser.mods.pitchperfect.packet.server_to_client

import dev.aaronhowser.mods.pitchperfect.packet.IModPacket
import dev.aaronhowser.mods.pitchperfect.util.ClientUtil
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class SpawnSparkParticlePacket(
    val x: Double,
    val y: Double,
    val z: Double,
) : IModPacket {
    override fun receiveMessage(context: IPayloadContext) {
        ClientUtil.spawnParticle(
            ParticleTypes.ELECTRIC_SPARK,
            x,
            y,
            z,
            0f, 0f, 0f
        )
    }

    override fun type(): CustomPacketPayload.Type<SpawnSparkParticlePacket> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SpawnSparkParticlePacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("spawn_spark"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SpawnSparkParticlePacket> =
            StreamCodec.composite(
                ByteBufCodecs.DOUBLE, SpawnSparkParticlePacket::x,
                ByteBufCodecs.DOUBLE, SpawnSparkParticlePacket::y,
                ByteBufCodecs.DOUBLE, SpawnSparkParticlePacket::z,
                ::SpawnSparkParticlePacket
            )
    }

}