package dev.aaronhowser.mods.pitchperfect.enchantment

import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object BwaaapEnchantment {

    fun trigger(user: LivingEntity, itemStack: ItemStack) {
        BwaaapWave(user, itemStack)
    }

    private class BwaaapWave(
        val user: LivingEntity,
        val itemStack: ItemStack
    ) {

        private val range = ServerConfig.BWAAAP_RANGE.get()
        private val targets: List<LivingEntity> = OtherUtil.getNearbyLivingEntities(user, range)

        init {
            knockback()
        }

        private fun knockback() {
            val strength = ServerConfig.BWAAAP_STRENGTH.get()
            var cooldown = 0.0

            for (target in targets) {
                val targetMotion = target.deltaMovement

                val toTargetVec = target.position().subtract(user.position())

                val distanceToTarget = user.distanceTo(target)
                val percentFromRange = 1 - distanceToTarget / range // 1 = at user, 0 = at range

                cooldown += percentFromRange

                val yMult = if (user.isCrouching) 2f else 1f

                target.deltaMovement = targetMotion.add(
                    toTargetVec.multiply(
                        percentFromRange * strength,
                        percentFromRange * strength * yMult,
                        percentFromRange * strength
                    )
                )
            }

            cooldown *= ServerConfig.BWAAAP_COOLDOWN_FACTOR.get()

            if (user is Player) {
                user.cooldowns.addCooldown(itemStack.item, Mth.ceil(cooldown))
            }
        }

    }

}