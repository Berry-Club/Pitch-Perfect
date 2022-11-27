package com.aaronhowser1.pitchperfect.client;

import com.aaronhowser1.pitchperfect.config.CommonConfigs;
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
        entities.forEach(entity -> {
            livingEntities.add((LivingEntity) entity);
        });
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
        return nearestEntity;
    }

}
