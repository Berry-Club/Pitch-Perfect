package com.aaronhowser1.pitchperfect.utils;

import com.aaronhowser1.pitchperfect.packets.ModPacketHandler;
import com.aaronhowser1.pitchperfect.packets.SpawnElectricPathPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ServerUtils {

    public static Vec3 entityToVec3(LivingEntity livingEntity) {
        return new Vec3(
                livingEntity.getX(),livingEntity.getY(),livingEntity.getZ()
        );
    }

    public static List<LivingEntity> getNearbyLivingEntities(LivingEntity livingEntity, int range) {
        List<Entity> entities = livingEntity.getLevel().getEntities(
                livingEntity,
                new AABB(
                        livingEntity.getX() - range,
                        livingEntity.getY() - range,
                        livingEntity.getZ() - range,
                        livingEntity.getX() + range,
                        livingEntity.getY() + range,
                        livingEntity.getZ() + range
                ),
                (e) -> (e instanceof LivingEntity)
        );
        List<LivingEntity> livingEntities = new ArrayList<>();
        for (Entity entity : entities) livingEntities.add((LivingEntity) entity);
        return livingEntities;
    }

    @Nullable
    public static LivingEntity getNearestEntity(List<LivingEntity> entities, LivingEntity originEntity) {
        if (entities.isEmpty()) return null;
        LivingEntity nearestEntity = entities.get(0);
        for (LivingEntity checkedEntity : entities) {
            if (checkedEntity != originEntity) {
                if (originEntity.distanceTo(nearestEntity) > originEntity.distanceTo(checkedEntity)) {
                    nearestEntity = checkedEntity;
                }
            }
        }
        if (nearestEntity == originEntity) return null;
        return nearestEntity;
    }

    public static void spawnElectricParticleLine(Vec3 origin, Vec3 destination, ServerLevel serverLevel) {
        ModPacketHandler.messageNearbyPlayers(
                new SpawnElectricPathPacket(
                        origin.x(),origin.y(),origin.z(),
                        destination.x(),destination.y(),destination.z()
                ),
                serverLevel,
                new Vec3(origin.x(),origin.y(),origin.z()),
                64
        );
    }

    public static double distanceBetweenPoints(Vec3 first, Vec3 second) {
        Vec3 userToTargetVector = vecBetweenPoints(first, second);

        return userToTargetVector.length();
    }

    public static Vec3 vecBetweenPoints(Vec3 first, Vec3 second) {
        return new Vec3(
                second.x()-first.x(),
                second.y()-first.y(),
                second.z()-first.z()
        );
    }
}
