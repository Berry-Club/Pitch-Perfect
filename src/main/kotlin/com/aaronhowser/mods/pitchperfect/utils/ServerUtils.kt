package com.aaronhowser.mods.pitchperfect.utils

import com.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import com.aaronhowser.mods.pitchperfect.packet.SpawnElectricPathPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object ServerUtils {

    fun entityToVec3(livingEntity: LivingEntity): Vec3 {
        return Vec3(
            livingEntity.x, livingEntity.y, livingEntity.z
        )
    }

    fun getNearbyLivingEntities(livingEntity: LivingEntity, range: Float): List<LivingEntity> {
        return livingEntity.getLevel().getEntities(
            livingEntity,
            AABB(
                livingEntity.x - range,
                livingEntity.y - range,
                livingEntity.z - range,
                livingEntity.x + range,
                livingEntity.y + range,
                livingEntity.z + range
            )
        ).filterIsInstance<LivingEntity>()
    }

    fun getNearestEntityInList(entities: List<LivingEntity>, originEntity: LivingEntity): LivingEntity? {
        return entities
            .filter { it != originEntity && it.isAlive }
            .minByOrNull { originEntity.distanceToSqr(it) }
    }

    fun spawnElectricParticleLine(origin: Vec3, destination: Vec3, serverLevel: ServerLevel) {
        ModPacketHandler.messageNearbyPlayers(
            SpawnElectricPathPacket(
                origin.x(), origin.y(), origin.z(),
                destination.x(), destination.y(), destination.z()
            ),
            serverLevel,
            Vec3(origin.x(), origin.y(), origin.z()),
            64.0
        )
    }

    fun distanceBetweenPoints(first: Vec3, second: Vec3): Double {
        val userToTargetVector = vecBetweenPoints(first, second)
        return userToTargetVector.length()
    }

    fun vecBetweenPoints(first: Vec3, second: Vec3): Vec3 {
        return Vec3(
            second.x() - first.x(),
            second.y() - first.y(),
            second.z() - first.z()
        )
    }

}