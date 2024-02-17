package dev.aaronhowser.pitchperfect.packet

import dev.aaronhowser.pitchperfect.utils.ClientUtils.spawnParticle
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class SpawnElectricParticlePacket(
    private val x: Double,
    private val y: Double,
    private val z: Double
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeDouble(x)
        buffer.writeDouble(y)
        buffer.writeDouble(z)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): SpawnElectricParticlePacket {
            return SpawnElectricParticlePacket(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        spawnParticle(ParticleTypes.ANGRY_VILLAGER, x, y, z, 1f, 1f, 1f)
        context.get().packetHandled = true
    }

}