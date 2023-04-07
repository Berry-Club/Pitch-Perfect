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

        if (totalTravelTime == 0) {
            spawnEntireLine();
            return;
        }

        Vec3 pathVector = originPositionVec.vectorTo(destinationPositionVec);
        float pathSize = (float) pathVector.length();
        int totalParticleCount = (int) (pathSize * (float) particlesPerBlock);
        if (totalParticleCount == 0) return;
        float distanceBetweenParticles = 1F/(float)particlesPerBlock;

        Vec3 deltaVector = pathVector.scale((1F/totalParticleCount));

        int ticksPerParticle = Math.max(totalTravelTime/totalParticleCount,1);
        // Unfortunately, as spawning the next particle uses a tick scheduler, there's a maximum of 1 tick per particle
        // This may cause the particle wave to arrive significantly after the enchantment damages the next mob

        // 🦆
        // An example:
        // A mob at (0,10,0) is it, and the next targeted is at (3,10,0)
        // pathVector = (3,0,0)
        // pathSize = 3.0F
        // totalParticleCount = 3*3 = 9
        // distanceBetweenParticles = 1/3 = 0.333F

        System.out.println("test");

        spawnNextParticleInWave(deltaVector, ticksPerParticle, totalParticleCount);
    }

    public void spawnNextParticleInWave(Vec3 deltaVector, int ticksPerParticle, int totalParticleCount) {

        if (iteration >= totalParticleCount) return;

        double dx = deltaVector.x()*iteration;
        double dy = deltaVector.y()*iteration;
        double dz = deltaVector.z()*iteration;

        ClientUtils.spawnParticle(
                particleType,
                originPositionVec.x()+dx,
                originPositionVec.y()+dy,
                originPositionVec.z()+dz,
                1,1,1
        );

        iteration++;
        ModScheduler.scheduleSynchronisedTask(
                () -> spawnNextParticleInWave(deltaVector, ticksPerParticle, totalParticleCount),
                ticksPerParticle
        );
    }

    public void spawnEntireLine() {
        if (particlesPerBlock == 0) return;

        ModScheduler.scheduleSynchronisedTask(
                () -> {
                    Vec3 pathVector = originPositionVec.vectorTo(destinationPositionVec);
                    float pathSize = (float) pathVector.length();
                    int totalParticleCount = (int) (pathSize * (float) particlesPerBlock);

                    if (totalParticleCount == 0) return;

                    Vec3 deltaVector = pathVector.scale((1F/totalParticleCount));

                    for (int i = 1; i <= totalParticleCount; i++) {

                        double dx = deltaVector.x()*i;
                        double dy = deltaVector.y()*i;
                        double dz = deltaVector.z()*i;

//                        System.out.println(
//                                "Spawning particle " + i +" which is ["+dx+","+dy+","+dz+"] blocks away from the origin"
//                        );

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
