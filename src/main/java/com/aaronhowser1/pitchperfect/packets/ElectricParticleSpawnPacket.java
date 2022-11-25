package com.aaronhowser1.pitchperfect.packets;

import com.aaronhowser1.pitchperfect.client.ClientUtils;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ElectricParticleSpawnPacket implements ModPacket{

    private final float x;
    private final float y;
    private final float z;


    public ElectricParticleSpawnPacket(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
    }

    public static ElectricParticleSpawnPacket decode(FriendlyByteBuf buffer) {
        return new ElectricParticleSpawnPacket(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat()
        );
    }

    public void receiveMessage(Supplier<NetworkEvent.Context> context) {

        ClientUtils.spawnParticle(ParticleTypes.ANGRY_VILLAGER,x,y,z,1,1,1);
        context.get().setPacketHandled(true);
    }

}
