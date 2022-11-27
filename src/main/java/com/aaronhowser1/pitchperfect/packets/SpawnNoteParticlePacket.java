package com.aaronhowser1.pitchperfect.packets;

import com.aaronhowser1.pitchperfect.client.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpawnNoteParticlePacket implements ModPacket{

    private final ResourceLocation soundResourceLocation;
    private final float pitch;
    private final double x;
    private final double y;
    private final double z;


    public SpawnNoteParticlePacket(ResourceLocation soundResourceLocation, float pitch, double x, double y, double z) {
        this.soundResourceLocation = soundResourceLocation;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(soundResourceLocation);
        buffer.writeFloat(pitch);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    public static SpawnNoteParticlePacket decode(FriendlyByteBuf buffer) {
        return new SpawnNoteParticlePacket(
                buffer.readResourceLocation(),
                buffer.readFloat(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
    }

    public void receiveMessage(Supplier<NetworkEvent.Context> context) {

        ClientUtils.spawnNote(pitch, x, y, z);

        context.get().setPacketHandled(true);
    }

}
