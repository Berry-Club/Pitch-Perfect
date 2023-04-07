package com.aaronhowser1.pitchperfect.utils;


import com.aaronhowser1.pitchperfect.config.ClientConfigs;
import com.aaronhowser1.pitchperfect.config.CommonConfigs;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

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
        this.particlesPerBlock = ClientConfigs.ELECTRIC_PARTICLE_DENSITY.get();
        this.totalTravelTime = CommonConfigs.ELECTRIC_JUMPTIME.get();
    }

    public void spawnWave() {

        if (particlesPerBlock == 0) return;

        Vec3 pathVector = originPositionVec.vectorTo(destinationPositionVec);
        float pathSize = (float) pathVector.length();
        int totalParticleCount = (int) (pathSize * (float) particlesPerBlock);
        float distanceBetweenParticles = 1F/(float)particlesPerBlock;
        Vec3 pathUnitVector = pathVector.normalize();
        Vec3 pathDeltaVector = new Vec3(
                pathUnitVector.x()*distanceBetweenParticles,
                pathUnitVector.y()*distanceBetweenParticles,
                pathUnitVector.z()*distanceBetweenParticles
        );

        int timePerParticle = Math.max(totalTravelTime/totalParticleCount,1);
        // Unfortunately, as spawning the next particle uses a tick scheduler, there's a maximum of 1 tick per particle
        // This may cause the particle wave to arrive significantly after the enchantment damages the next mob

        // 🦆
        // An example:
        // A mob at (0,10,0) is it, and the next targeted is at (3,10,0)
        // pathVector = (3,0,0)
        // pathSize = 3.0F
        // totalParticleCount = 3*3 = 9
        // distanceBetweenParticles = 1/3 = 0.333F

//        System.out.println("Path Size:\n"+pathSize+"\nDistance Between:\n"+distanceBetweenParticles+"\nParticle Count:\n"+totalParticleCount);

        spawnNextParticleInWave(totalParticleCount, pathDeltaVector, originPositionVec, distanceBetweenParticles, timePerParticle);
    }

    public void spawnNextParticleInWave(int totalParticleCount, Vec3 deltaVector, Vec3 newOriginVec, float distanceBetween, int ticksPerParticle) {
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
                    () -> spawnNextParticleInWave(totalParticleCount, deltaVector, particlePositionVector, distanceBetween, ticksPerParticle),
                    ticksPerParticle
            );
        }
    }

    public void spawnEntireLine() {
        if (particlesPerBlock == 0) return;

        ModScheduler.scheduleSynchronisedTask(
                () -> {
                    Vec3 pathVector = originPositionVec.vectorTo(destinationPositionVec);
                    float pathSize = (float) pathVector.length();
                    int totalParticleCount = (int) (pathSize * (float) particlesPerBlock);
                    float distanceBetweenParticles = 1F/(float)particlesPerBlock;

                    Vec3 pathUnitVector = pathVector.normalize();

                    for (int i = 1; i <= totalParticleCount; i++) {

                        double dx = pathUnitVector.x()*i;
                        double dy = pathUnitVector.y()*i;
                        double dz = pathUnitVector.z()*i;

                        ClientUtils.spawnParticle(
                                particleType,
                                originPositionVec.x()+dx,
                                originPositionVec.y()+dy,
                                originPositionVec.z()+dz,
                                1,1,1
                        );

                    }
                },
                totalTravelTime
        );
    }
}
