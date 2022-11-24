package com.aaronhowser1.pitchperfect.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NoteParticleSpawnPacket {

    private final float red;
    private final float green;
    private final float blue;
    private final float x;
    private final float y;
    private final float z;


    public NoteParticleSpawnPacket(float red, float green, float blue, float x, float y, float z) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(red);
        buffer.writeFloat(green);
        buffer.writeFloat(blue);
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
    }

    public static NoteParticleSpawnPacket decode(FriendlyByteBuf buffer) {
        return new NoteParticleSpawnPacket(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat()
        );
    }

    public void receiveMessage(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

}
