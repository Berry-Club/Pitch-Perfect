package dev.aaronhowser.mods.pitchperfect.util

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB

object OtherUtil {

    fun Float.map(min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (max2 - min2) * ((this - min1) / (max1 - min1))
    }

    fun modResource(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(PitchPerfect.ID, path)
    }

    fun getNearbyLivingEntities(originEntity: LivingEntity, range: Double): List<LivingEntity> {
        return originEntity.level().getEntities(
            originEntity,
            AABB(
                originEntity.x - range,
                originEntity.y - range,
                originEntity.z - range,
                originEntity.x + range,
                originEntity.y + range,
                originEntity.z + range
            )
        ).filterIsInstance<LivingEntity>()
    }

}