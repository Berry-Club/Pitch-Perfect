package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.utils.ServerUtils.distanceBetweenPoints
import com.aaronhowser1.pitchperfect.utils.ServerUtils.entityToVec3
import com.aaronhowser1.pitchperfect.utils.ServerUtils.getNearbyLivingEntities
import com.aaronhowser1.pitchperfect.utils.ServerUtils.vecBetweenPoints
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.Enchantment
import kotlin.math.abs
import kotlin.math.roundToInt

object BwaaapEnchantment : Enchantment(
    Rarity.COMMON,
    ModEnchantments.INSTRUMENT,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {

    override fun getMinCost(pLevel: Int): Int = 5
    override fun getMaxCost(pLevel: Int): Int = 25

    fun triggerBwaaap(user: LivingEntity, instrument: InstrumentItem) {
        BwaaapTrigger(user, instrument)
    }

    private class BwaaapTrigger(val user: LivingEntity, instrument: InstrumentItem) {
        private val targets: List<LivingEntity> = getTargets()
        private val cooldown: Int = getCooldown()

        private val range: Float = ServerConfig.BWAAAP_RANGE.get()


        init {
            knockBack()
            if (user is Player) user.cooldowns.addCooldown(instrument, cooldown)
        }

        private fun getTargets(): List<LivingEntity> {
            return getNearbyLivingEntities(user, range)
        }

        private fun getCooldown(): Int {
            var cooldown = 0f

            for (target in targets) {
                val distanceToTarget = distanceBetweenPoints(
                    entityToVec3(user),
                    entityToVec3(target)
                )
                if (distanceToTarget > range) continue

                val targetPercentageFromRange = abs(distanceToTarget / range - 1)
                cooldown += targetPercentageFromRange.toFloat()
            }

            cooldown *= ServerConfig.BWAAAP_COOLDOWN_MULT.get()
            return cooldown.roundToInt()
        }

        private fun knockBack() {
            val strength: Float = ServerConfig.BWAAAP_STRENGTH.get()

            for (target in targets) {
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

}