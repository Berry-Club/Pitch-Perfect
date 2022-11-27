package com.aaronhowser1.pitchperfect.utils;


import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

//Probably a really bad way to do this but I am sleep deprived and out of ideas
public class ParticleLine {

    private Vec3 originVec;
    private Vec3 destinationVec;
    private SimpleParticleType particleType;
    private int iteration;
    private int particlesPerBlock;
    private long totalTravelTime;

    public ParticleLine(Vec3 originVec, Vec3 destinationVec, SimpleParticleType particleType) {
        this.originVec = originVec;
        this.destinationVec = destinationVec;
        this.particleType = particleType;
        this.iteration = 1;
        this.particlesPerBlock = 9;
        this.totalTravelTime = CommonConfigs.ELECTRIC_JUMPTIME.get();
    }

    public void spawnLine() {
        Vec3 pathVector = originVec.vectorTo(destinationVec);
        float pathSize = (float) pathVector.length();
        float distanceBetween = pathSize/particlesPerBlock;
        int totalParticleCount = (int) (pathSize * (float) particlesPerBlock);
        Vec3 pathUnitVector = pathVector.normalize();
        long timePerParticle = totalTravelTime/totalParticleCount;

        spawnNextParticle(totalParticleCount, pathUnitVector, originVec, distanceBetween, timePerParticle);
    }

    public void spawnNextParticle(int totalParticleCount, Vec3 deltaVector, Vec3 newOriginVec, float distanceBetween, long timePerParticle) {
        if (iteration <= totalParticleCount) {
            Vec3 particlePositionVector = newOriginVec.add(
                    deltaVector.x()*distanceBetween,
                    deltaVector.y()*distanceBetween,
                    deltaVector.z()*distanceBetween
            );

            ClientUtils.spawnParticle(
                    particleType,
                    particlePositionVector.x(),
                    particlePositionVector.y(),
                    particlePositionVector.z(),
                    1,1,1
            );

            this.iteration++;

            Util.backgroundExecutor().submit( () -> {
                try {
                    Thread.sleep(timePerParticle);
                } catch (Exception ignored) {
                }
                spawnNextParticle(totalParticleCount, deltaVector, particlePositionVector, distanceBetween,timePerParticle);
            });
        }
    }

//    private void spawnNextParticle(int iteration, double x, double y, double z, long waitTime, int totalParticleCount) {
//        if (iteration <= totalParticleCount) {
//            Vec3 particleVector = origin.add(
//                    pathUnitVector.x()*distanceBetween*iteration,
//                    pathUnitVector.y()*distanceBetween*iteration,
//                    pathUnitVector.z()*distanceBetween*iteration
//            );
//            double particleX = particleVector.x();
//            double particleY = particleVector.y();
//            double particleZ = particleVector.z();
//        }
//        ClientUtils.spawnParticle(ParticleTypes.ANGRY_VILLAGER,z,y,x,1,1,1);
//        Util.backgroundExecutor().submit( () -> {
//            try {
//                Thread.sleep(waitTime);
//            } catch (Exception ignored) {
//            }
//            spawnNextParticle(origin,destination,iteration+1);
//        };

}
