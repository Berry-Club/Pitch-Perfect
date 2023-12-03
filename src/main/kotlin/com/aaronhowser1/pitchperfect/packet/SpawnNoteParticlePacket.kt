package com.aaronhowser1.pitchperfect.packet

import com.aaronhowser1.pitchperfect.utils.ClientUtils.spawnNote
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class SpawnNoteParticlePacket(
    private val soundResourceLocation: ResourceLocation,
    private val pitch: Float,
    private val x: Double,
    private val y: Double,
    private val z: Double
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeResourceLocation(soundResourceLocation)
        buffer.writeFloat(pitch)
        buffer.writeDouble(x)
        buffer.writeDouble(y)
        buffer.writeDouble(z)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): SpawnNoteParticlePacket {
            return SpawnNoteParticlePacket(
                buffer.readResourceLocation(),
                buffer.readFloat(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        spawnNote(pitch, x, y, z)
        context.get().packetHandled = true
    }


}