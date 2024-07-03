package dev.aaronhowser.mods.pitchperfect.enchantment

import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.util.ModScheduler
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent
import kotlin.math.pow

object AndHisMusicWasElectricEnchantment {

    private val currentElectricAttacks: MutableSet<ElectricAttack> = mutableSetOf()

    fun handleElectric(event: LivingHurtEvent) {
        if (event.isCanceled) return
        if (event.amount < 1f) return

        val source = event.source
        val attacker = source.entity as? LivingEntity ?: return

        if (attacker.level().isClientSide) return

        if (currentElectricAttacks.any { it.event.source == source }) return

        fun ItemStack.hasElectric() = getEnchantmentLevel(
            ModEnchantments.getEnchantHolder(
                attacker.level(),
                ModEnchantments.andHisMusicWasElectricResourceKey
            )
        ) != 0

        val electricItemStack =
            if (attacker is Player) {
                attacker.inventory.items.firstOrNull {
                    it.hasElectric()
                }
            } else {
                attacker.allSlots.firstOrNull { it.hasElectric() }
            }

        if (electricItemStack == null) return

        ElectricAttack(event, attacker, electricItemStack)
    }

    private class ElectricAttack(
        val event: LivingHurtEvent,
        val attacker: LivingEntity,
        val itemStack: ItemStack
    ) {
        private val initialTarget: LivingEntity = event.entity

        private val entitiesToAvoid: MutableSet<LivingEntity> = mutableSetOf(initialTarget, attacker)
        private var iteration: Int = 0

        private val attackerIsMonster = attacker is Monster
        private val targetIsMonster = initialTarget is Monster

        private fun end() {
            currentElectricAttacks.remove(this)
        }

        init {
            currentElectricAttacks.add(this)

            hitNextTarget(initialTarget)
        }

        private fun hitNextTarget(currentTarget: LivingEntity) {

            val nextTarget = getNearestTarget(currentTarget)
            if (nextTarget == null) {
                end()
                return
            }

            if (iteration > ServerConfig.ELECTRIC_MAX_JUMPS.get()) {
                end()
                return
            }
            iteration++

            if (attacker is Player) {
                itemStack.hurtAndBreak(1, attacker, attacker.getEquipmentSlotForItem(itemStack))
            }

            ModScheduler.scheduleTaskInTicks(ServerConfig.ELECTRIC_JUMP_TIME.get()) {
                damage(nextTarget)
            }

        }

        private fun damage(
            targetEntity: LivingEntity
        ) {
            if (targetEntity.isDeadOrDying) {
                end()
                return
            }

            val damage = event.amount * ServerConfig.ELECTRIC_DAMAGE_FACTOR.get().pow(iteration).toFloat()
            if (damage < 0.5f) {
                end()
                return
            }

            targetEntity.hurt(event.source, damage)

            hitNextTarget(targetEntity)
        }

        private fun getNearestTarget(currentTarget: LivingEntity): LivingEntity? {
            return getNearbyTargets(currentTarget).minByOrNull { it.distanceToSqr(currentTarget) }
        }

        private fun getNearbyTargets(currentTarget: LivingEntity): List<LivingEntity> {

            val nearbyTargets = OtherUtil.getNearbyLivingEntities(
                currentTarget,
                ServerConfig.ELECTRIC_RANGE.get()
            ).filter { !it.isDeadOrDying && it !in entitiesToAvoid }.toMutableSet()

            // If the attacker is not a monster, and a monster is attacked, aim only at monsters
            // If the attacker is not a monster, and a non-monster is attacked, aim at anything nearby
            // If the attacker is a monster, and a non-monster is attacked, aim only at non-monsters
            // If the attacker is a monster, and a monster is attacked, aim at anything nearby

            if (!attackerIsMonster && targetIsMonster) {
                nearbyTargets.removeIf { it !is Monster }
            } else if (attackerIsMonster && !targetIsMonster) {
                nearbyTargets.removeIf { it is Monster }
            }

            return nearbyTargets.toList()
        }

    }

}