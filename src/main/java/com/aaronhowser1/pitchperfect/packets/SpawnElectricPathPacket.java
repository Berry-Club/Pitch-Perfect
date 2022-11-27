package com.aaronhowser1.pitchperfect.packets;

import com.aaronhowser1.pitchperfect.utils.ClientUtils;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpawnElectricPathPacket implements ModPacket{

    private final double x1;
    private final double y1;
    private final double z1;
    private final double x2;
    private final double y2;
    private final double z2;


    public SpawnElectricPathPacket(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeDouble(x1);
        buffer.writeDouble(y1);
        buffer.writeDouble(z1);
        buffer.writeDouble(x2);
        buffer.writeDouble(y2);
        buffer.writeDouble(z2);
    }

    public static SpawnElectricPathPacket decode(FriendlyByteBuf buffer) {
        return new SpawnElectricPathPacket(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
    }

    public void receiveMessage(Supplier<NetworkEvent.Context> context) {

        Vec3 originVec = new Vec3(x1,y1,z1);
        Vec3 destinationVec = new Vec3(x2,y2,z2);

        spawnParticlePath(originVec,destinationVec,1);
        context.get().setPacketHandled(true);
    }


    private static void spawnParticlePath(Vec3 origin, Vec3 destination, int iteration) {

        int particlesPerBlock = 9;
        Vec3 pathVector = origin.vectorTo(destination);
        float pathSize = (float) pathVector.length();
        int totalParticleCount = (int) (pathSize * particlesPerBlock);
        Vec3 pathUnitVector = pathVector.normalize();
        long totalTravelTime = CommonConfigs.ELECTRIC_JUMPTIME.get();

        if (iteration <= totalParticleCount) {
            Vec3 particleVector = origin.add(
                    pathUnitVector.x()*iteration,
                    pathUnitVector.y()*iteration,
                    pathUnitVector.z()*iteration
            );
            double particleX = particleVector.x();
            double particleY = particleVector.y();
            double particleZ = particleVector.z();
            ClientUtils.spawnParticle(ParticleTypes.ANGRY_VILLAGER,particleX,particleY,particleZ,1,1,1);
            Util.backgroundExecutor().submit( () -> {
                try {
                    Thread.sleep(totalTravelTime/totalParticleCount);
                } catch (Exception ignored) {
                }
                spawnParticlePath(origin,destination,iteration+1);
            });
        }
    }
}
