package com.aaronhowser1.pitchperfect.client;

import com.aaronhowser1.pitchperfect.config.CommonConfigs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ServerUtils {

    public static Vec3 entityToVec3(LivingEntity livingEntity) {
        return new Vec3(
                livingEntity.getX(),livingEntity.getY(),livingEntity.getZ()
        );
    }

    public static List<LivingEntity> getNearbyLivingEntities(LivingEntity livingEntity) {
        List<Entity> entities = livingEntity.getLevel().getEntities(
                livingEntity,
                new AABB(
                        livingEntity.getX() - CommonConfigs.ELECTRIC_RANGE.get(),
                        livingEntity.getY() - CommonConfigs.ELECTRIC_RANGE.get(),
                        livingEntity.getZ() - CommonConfigs.ELECTRIC_RANGE.get(),
                        livingEntity.getX() + CommonConfigs.ELECTRIC_RANGE.get(),
                        livingEntity.getY() + CommonConfigs.ELECTRIC_RANGE.get(),
                        livingEntity.getZ() + CommonConfigs.ELECTRIC_RANGE.get()
                ),
                (e) -> (e instanceof LivingEntity)
        );
        List<LivingEntity> livingEntities = new ArrayList<>();
        entities.forEach(entity -> {
            livingEntities.add((LivingEntity) entity);
        });
        return livingEntities;
    }

}
