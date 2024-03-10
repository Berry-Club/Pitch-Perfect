package com.aaronhowser.pitchperfect.packet

import com.aaronhowser.pitchperfect.config.ClientConfig
import com.aaronhowser.pitchperfect.utils.ParticleLine
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class SpawnElectricPathPacket (
    val startX: Double,
    val startY: Double,
    val startZ: Double,
    val endX: Double,
    val endY: Double,
    val endZ: Double
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeDouble(startX)
        buffer.writeDouble(startY)
        buffer.writeDouble(startZ)
        buffer.writeDouble(endX)
        buffer.writeDouble(endY)
        buffer.writeDouble(endZ)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): SpawnElectricPathPacket {
            return SpawnElectricPathPacket(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        val originVec = Vec3(startX, startY, startZ)
        val destinationVec = Vec3(endX, endY, endZ)
        spawnParticlePath(originVec, destinationVec)
        context.get().packetHandled = true
    }


    private fun spawnParticlePath(origin: Vec3, destination: Vec3) {
        val particleLine = ParticleLine(origin, destination, ParticleTypes.ANGRY_VILLAGER)
        val isWave: Boolean = ClientConfig.ELECTRIC_PARTICLE_ISWAVE.get()
        if (isWave) {
            particleLine.spawnWave()
        } else {
            particleLine.spawnEntireLine()
        }
    }

}