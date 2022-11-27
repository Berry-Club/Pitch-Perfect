package com.aaronhowser1.pitchperfect.utils;


import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import net.minecraft.Util;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

//Probably a really bad way to do this but I am sleep deprived and out of ideas
public class ParticleLine {

    private final Vec3 originPositionVec;
    private final Vec3 destinationPositionVec;
    private final SimpleParticleType particleType;
    private int iteration;
    private final int particlesPerBlock;
    private final int totalTravelTime;

    public ParticleLine(Vec3 originPositionVec, Vec3 destinationPositionVec, SimpleParticleType particleType) {
        this.originPositionVec = originPositionVec;
        this.destinationPositionVec = destinationPositionVec;
        this.particleType = particleType;
        this.iteration = 1;
        this.particlesPerBlock = 9;
        this.totalTravelTime = CommonConfigs.ELECTRIC_JUMPTIME.get();
    }

    public void spawnLine() {
        Vec3 pathVector = originPositionVec.vectorTo(destinationPositionVec);
        float pathSize = (float) pathVector.length();
        float distanceBetween = pathSize/particlesPerBlock;
        int totalParticleCount = (int) (pathSize * (float) particlesPerBlock);
        Vec3 pathUnitVector = pathVector.normalize();
        Vec3 pathDeltaVector = new Vec3(
                pathUnitVector.x()*distanceBetween,
                pathUnitVector.y()*distanceBetween,
                pathUnitVector.z()*distanceBetween
        );
        int timePerParticle = totalTravelTime/totalParticleCount;

        System.out.println("Path Size:\n"+pathSize+"\nDistance Between:\n"+distanceBetween+"\nParticle Count:\n"+totalParticleCount);

        spawnNextParticle(totalParticleCount, pathDeltaVector, originPositionVec, distanceBetween, timePerParticle);
    }

    public void spawnNextParticle(int totalParticleCount, Vec3 deltaVector, Vec3 newOriginVec, float distanceBetween, int ticksPerParticle) {
        if (iteration <= totalParticleCount) {
            Vec3 particlePositionVector = newOriginVec.add(deltaVector);

            ClientUtils.spawnParticle(
                    particleType,
                    particlePositionVector.x(),
                    particlePositionVector.y(),
                    particlePositionVector.z(),
                    1,1,1
            );

            this.iteration++;

            ModScheduler.scheduleSynchronisedTask(
                    () -> spawnNextParticle(totalParticleCount, deltaVector, particlePositionVector, distanceBetween,ticksPerParticle),
                    ticksPerParticle
            );
        }
    }

}
