package com.aaronhowser.pitchperfect.packet

import com.aaronhowser.pitchperfect.utils.ClientUtils.playNote
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class SpawnNotePacket(
    private val soundResourceLocation: ResourceLocation,
    private val pitch: Float,
    private val x: Double,
    private val y: Double,
    private val z: Double,
    private val hasBwaaap: Boolean = false
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeResourceLocation(soundResourceLocation)
        buffer.writeFloat(pitch)
        buffer.writeDouble(x)
        buffer.writeDouble(y)
        buffer.writeDouble(z)
        buffer.writeBoolean(hasBwaaap)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): SpawnNotePacket {
            return SpawnNotePacket(
                buffer.readResourceLocation(),
                buffer.readFloat(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readBoolean()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        playNote(soundResourceLocation, pitch, x, y, z, hasBwaaap)
        context.get().packetHandled = true
    }


}