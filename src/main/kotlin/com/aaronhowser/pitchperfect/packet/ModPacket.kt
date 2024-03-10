package com.aaronhowser.pitchperfect.packet

import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

interface ModPacket {

    fun encode(buffer: FriendlyByteBuf)
    fun receiveMessage(context: Supplier<NetworkEvent.Context>)

}