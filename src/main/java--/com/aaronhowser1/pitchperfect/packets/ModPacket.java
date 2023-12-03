package com.aaronhowser1.pitchperfect.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface ModPacket {

    void encode(FriendlyByteBuf buffer);
    void receiveMessage(Supplier<NetworkEvent.Context> context);
}
