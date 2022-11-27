package com.aaronhowser1.pitchperfect.packets;

import com.aaronhowser1.pitchperfect.client.ClientUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpawnElectricParticlePacket implements ModPacket{

    private final double x;
    private final double y;
    private final double z;


    public SpawnElectricParticlePacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    public static SpawnElectricParticlePacket decode(FriendlyByteBuf buffer) {
        return new SpawnElectricParticlePacket(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
    }

    public void receiveMessage(Supplier<NetworkEvent.Context> context) {

        ClientUtils.spawnParticle(ParticleTypes.ANGRY_VILLAGER,x,y,z,1,1,1);
        context.get().setPacketHandled(true);
    }

}
