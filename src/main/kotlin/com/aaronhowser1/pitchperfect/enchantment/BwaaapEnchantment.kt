package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.utils.ServerUtils.distanceBetweenPoints
import com.aaronhowser1.pitchperfect.utils.ServerUtils.entityToVec3
import com.aaronhowser1.pitchperfect.utils.ServerUtils.getNearbyLivingEntities
import com.aaronhowser1.pitchperfect.utils.ServerUtils.vecBetweenPoints
import net.minecraft.util.Mth
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.enchantment.Enchantment
import kotlin.math.abs

object BwaaapEnchantment : Enchantment(
    Rarity.COMMON,
    ModEnchantments.INSTRUMENT,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {

    override fun getMinCost(pLevel: Int): Int {
        return 5
    }

    override fun getMaxCost(pLevel: Int): Int {
        return 25
    }

    private fun getTargets(user: LivingEntity): List<LivingEntity> {
        val range: Int = ServerConfig.BWAAAP_RANGE.get()
        return getNearbyLivingEntities(user, range)
    }

    fun getCooldown(user: LivingEntity): Int {
        var cooldown = 0f

        for (target in getTargets(user)) {
            val range: Int = ServerConfig.BWAAAP_RANGE.get()

            val distanceToTarget = distanceBetweenPoints(
                entityToVec3(user),
                entityToVec3(target)
            )
            if (distanceToTarget > range) continue

            val targetPercentageFromRange = abs(distanceToTarget / range - 1)
            cooldown += targetPercentageFromRange.toFloat()
        }

        cooldown *= ServerConfig.BWAAAP_COOLDOWN_MULT.get()
        return Mth.floor(cooldown)
    }

    fun knockBack(user: LivingEntity) {
        val range: Int = ServerConfig.BWAAAP_RANGE.get()
        val strength: Float = ServerConfig.BWAAAP_STRENGTH.get()

        for (target in getTargets(user)) {
            val targetMotion = target.deltaMovement

            val userToTargetVector = vecBetweenPoints(
                entityToVec3(user),
                entityToVec3(target)
            )

            val distanceToTarget = userToTargetVector.length()
            if (distanceToTarget > range) continue

            val targetPercentageFromRange = abs(distanceToTarget / range - 1)
            val yMult = if (user.isCrouching) 2f else 1f

            target.deltaMovement = targetMotion.add(
                userToTargetVector.multiply(
                    targetPercentageFromRange * strength,
                    targetPercentageFromRange * strength * yMult,
                    targetPercentageFromRange * strength
                )
            )
        }
    }

}