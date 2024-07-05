package dev.aaronhowser.mods.pitchperfect.enchantment

import dev.aaronhowser.mods.pitchperfect.config.ClientConfig
import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.registry.ModSounds
import dev.aaronhowser.mods.pitchperfect.util.ModScheduler
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
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

            ElectricLine(
                currentTarget.x,
                currentTarget.y,
                currentTarget.z,
                nextTarget.x,
                nextTarget.y,
                nextTarget.z,
                ClientConfig.ELECTRIC_PARTICLE_IS_WAVE.get(),
                attacker.level()
            )

            if (attacker is Player) {
                val level = attacker.level() as ServerLevel
                itemStack.hurtAndBreak(80, level, attacker) {
                    level.playSound(
                        null,
                        attacker.blockPosition(),
                        ModSounds.GUITAR_SMASH.get(),
                        SoundSource.PLAYERS
                    )
                }
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

    class ElectricLine(
        val x1: Double,
        val y1: Double,
        val z1: Double,
        val x2: Double,
        val y2: Double,
        val z2: Double,
        val isWave: Boolean,
        val level: Level
    ) {
        private val particlesPerBlock: Int = ClientConfig.ELECTRIC_PARTICLE_DENSITY.get()
        private val totalTravelTime: Int = ServerConfig.ELECTRIC_JUMP_TIME.get()

        init {
            if (isWave) {
                spawnWave()
            } else {
                spawnLine()
            }
        }

        private fun spawnWave() {

            if (particlesPerBlock < 1) return
            if (totalTravelTime < 1) {
                spawnLine()
                return
            }

            val pathVector = Vec3(x2 - x1, y2 - y1, z2 - z1)
            val pathLength = pathVector.length()

            val totalParticleCount = Mth.floor(pathLength * particlesPerBlock)
            if (totalParticleCount < 1) return

            val ticksPerParticle = totalTravelTime.toDouble() / totalParticleCount

            for (i in 0 until totalParticleCount) {
                val delay = Mth.floor(i * ticksPerParticle)

                val percent = i.toDouble() / totalParticleCount
                val deltaVec = pathVector.scale(percent)
                val particleLoc = Vec3(x1, y1, z1).add(deltaVec)

                ModScheduler.scheduleTaskInTicks(delay) {


                }

            }


        }

        private fun spawnLine() {

        }

    }

}