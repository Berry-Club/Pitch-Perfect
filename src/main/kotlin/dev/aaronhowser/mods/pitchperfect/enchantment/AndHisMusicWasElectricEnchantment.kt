package dev.aaronhowser.mods.pitchperfect.enchantment

import com.aaronhowser.mods.pitchperfect.config.ServerConfig
import com.aaronhowser.mods.pitchperfect.event.ModScheduler
import com.aaronhowser.mods.pitchperfect.item.InstrumentItem
import com.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import com.aaronhowser.mods.pitchperfect.packet.SpawnElectricParticlePacket
import com.aaronhowser.mods.pitchperfect.utils.CommonUtils.asInstrument
import com.aaronhowser.mods.pitchperfect.utils.CommonUtils.hasEnchantment
import com.aaronhowser.mods.pitchperfect.utils.ServerUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.entity.living.LivingHurtEvent
import kotlin.math.min
import kotlin.math.pow

object AndHisMusicWasElectricEnchantment : Enchantment(
    Rarity.VERY_RARE,
    ModEnchantments.INSTRUMENT,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {

    override fun getMinCost(pLevel: Int): Int = 15
    override fun getMaxCost(pLevel: Int): Int = 55

    private val currentElectricAttacks: MutableSet<ElectricAttack> = mutableSetOf()

    fun handleElectric(event: LivingHurtEvent) {

        if (event.isCanceled) return

        val source: DamageSource = event.source
        val attacker: LivingEntity = source.entity as? LivingEntity ?: return

        if (currentElectricAttacks.any { it.damageSource == source }) return

        fun ItemStack.hasElectricEnchantment(): Boolean =
            this.item is InstrumentItem && this.hasEnchantment(ModEnchantments.AND_HIS_MUSIC_WAS_ELECTRIC.get())

        //Sets to the first InstrumentItem that has the enchantment in your inventory, or stays null
        val electricItemStack: ItemStack? =
            if (attacker is Player) {
                attacker.inventory.items.firstOrNull { it.hasElectricEnchantment() }
            } else {
                attacker.allSlots.firstOrNull { it.hasElectricEnchantment() }
            }

        if (electricItemStack == null) return
        if (attacker.level.isClientSide) return

        ElectricAttack(event, electricItemStack)

    }

    private class ElectricAttack(
        val event: LivingHurtEvent,
        val electricItemStack: ItemStack
    ) {
        val damageSource: DamageSource = event.source
        private val instrumentItem: InstrumentItem? = electricItemStack.item.asInstrument()

        private val attacker: LivingEntity? = damageSource.entity as? LivingEntity
        private val initialTarget: LivingEntity? = event.entity

        private val entitiesHit: MutableList<LivingEntity> = arrayListOf()
        private var iteration: Int = 0

        private val targetIsMonster: Boolean = initialTarget is Monster
        private val attackerIsMonster: Boolean = attacker is Monster

        private class EndDamage : Exception()

        init {
            currentElectricAttacks.add(this)

            if (initialTarget == null || attacker == null || instrumentItem == null) {
                endAttack()
            } else {

                try {
                    entitiesHit.apply {
                        add(initialTarget)
                        add(attacker)
                    }
                    hitNextTarget(initialTarget)
                } catch (e: EndDamage) {
                    endAttack()
                }
            }

        }

        private fun endAttack() {
            currentElectricAttacks.remove(this)
        }

        private fun hitNextTarget(currentTarget: LivingEntity) {

            if (iteration > ServerConfig.ELECTRIC_MAX_JUMPS.get()) throw EndDamage()

            val nextTarget: LivingEntity? = getNearestTarget(currentTarget)
            if (nextTarget == null) {
                endAttack()
                return
            }

            if (iteration++ == 1) damageItem()

            ServerUtils.spawnElectricParticleLine(
                Vec3(currentTarget.x, currentTarget.y, currentTarget.z),
                Vec3(nextTarget.x, nextTarget.y, nextTarget.z),
                nextTarget.getLevel() as ServerLevel
            )

            //Wait for the particles to reach
            ModScheduler.scheduleSynchronisedTask(ServerConfig.ELECTRIC_JUMP_TIME.get()) {
                damage(nextTarget)
            }
        }

        private fun damage(
            targetEntity: LivingEntity
        ) {
            if (!targetEntity.isAlive) throw EndDamage()

            spawnHurtParticles(targetEntity)
            hurtTarget(targetEntity)

            hitNextTarget(targetEntity)
        }

        private fun hurtTarget(targetEntity: LivingEntity) {
            val damage = event.amount * ServerConfig.ELECTRIC_DAMAGE_FACTOR.get().pow(iteration).toFloat()
            if (damage < 0.5) throw EndDamage()

            targetEntity.hurt(event.source, damage)

            instrumentItem?.attack(targetEntity)
        }

        private fun spawnHurtParticles(targetEntity: LivingEntity) {
            val entityWidth = targetEntity.bbWidth.toDouble()
            val entityHeight = targetEntity.bbHeight.toDouble()
            for (p in 1..min(iteration.toDouble(), 5.0).toInt()) {

                val x = targetEntity.x + entityWidth * (Math.random() * .75 - .375)
                val y = targetEntity.y + entityHeight + min(2.0, iteration * 0.05)
                val z = targetEntity.z + entityWidth * (Math.random() * .75 - .375)

                ModPacketHandler.messageNearbyPlayers(
                    SpawnElectricParticlePacket(x, y, z),
                    targetEntity.getLevel() as ServerLevel,
                    Vec3(x, y, z),
                    16.0
                )
            }
        }

        private fun damageItem() {
            if (attacker is Player) {
                electricItemStack.hurtAndBreak(1, attacker) { user: LivingEntity ->
                    user.getLevel().playSound(
                        null,
                        attacker.blockPosition(),
                        dev.aaronhowser.mods.pitchperfect.ModSounds.GUITAR_SMASH.get(),
                        SoundSource.PLAYERS,
                        1f,
                        1f
                    )
                }
            }
        }

        private fun getNearestTarget(currentTarget: LivingEntity): LivingEntity? {
            return getNearbyTargets(currentTarget).minByOrNull { currentTarget.distanceToSqr(it) }
        }

        private fun getNearbyTargets(currentTarget: LivingEntity): List<LivingEntity> {

            val nearbyTargets = ServerUtils.getNearbyLivingEntities(
                currentTarget, ServerConfig.ELECTRIC_RANGE.get().toFloat()
            ).filter { it.isAlive && it !in entitiesHit }.toMutableList()

            // If the attacker is not a monster, and a monster is attacked, aim only at monsters
            // If the attacker is not a monster, and a non-monster is attacked, aim at anything nearby
            // If the attacker is a monster, and a non-monster is attacked, aim only at non-monsters
            // If the attacker is a monster, and a monster is attacked, aim at anything nearby

            if (!attackerIsMonster && targetIsMonster) {
                nearbyTargets.removeIf { it !is Monster }
            } else if (attackerIsMonster && !targetIsMonster) {
                nearbyTargets.removeIf { it is Monster }
            }

            return nearbyTargets
        }

    }
}