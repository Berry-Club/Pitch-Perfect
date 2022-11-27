package com.aaronhowser1.pitchperfect.packets;

public class SpawnElectricPathPacket {



//    private static void spawnParticlePath(Entity originEntity, Vec3 destination) {
//
//        int particlesPerBlock = 9;
//        Vec3 origin = new Vec3(originEntity.getX(), originEntity.getY(), originEntity.getZ());
//        Vec3 pathVector = origin.vectorTo(destination);
//        float pathSize = (float) pathVector.length();
//        int totalParticleCount = (int) (pathSize * particlesPerBlock);
//        Vec3 pathUnitVector = pathVector.normalize();
//        long totalTravelTime = CommonConfigs.ELECTRIC_JUMPTIME.get();
//
//        int particleIteration = 1;
//        while (particleIteration <= totalParticleCount) {
//            Vec3 particleVector = origin.add(
//                    pathUnitVector.x()*particleIteration,
//                    pathUnitVector.y()*particleIteration,
//                    pathUnitVector.z()*particleIteration
//            );
//            ModPacketHandler.messageNearbyPlayers(
//                    new ElectricParticleSpawnPacket(particleVector.x(), particleVector.y(), particleVector.z()),
//                    (ServerLevel) originEntity.getLevel(),
//                    new Vec3(particleVector.x(), particleVector.y(), particleVector.z()),
//                    64
//            );
//            particleIteration++;
//        }
//    }
}
