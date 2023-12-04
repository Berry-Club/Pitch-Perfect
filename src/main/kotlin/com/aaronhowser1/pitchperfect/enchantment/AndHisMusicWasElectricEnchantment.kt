package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.event.ModScheduler
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.packet.ModPacketHandler
import com.aaronhowser1.pitchperfect.packet.SpawnElectricParticlePacket
import com.aaronhowser1.pitchperfect.utils.ServerUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.entity.living.LivingHurtEvent
import kotlin.math.min

object AndHisMusicWasElectricEnchantment : Enchantment(
    Rarity.VERY_RARE,
    ModEnchantments.INSTRUMENT,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {

    val currentElectricAttackers: MutableSet<LivingEntity> = mutableSetOf()

    override fun getMinCost(pLevel: Int): Int = 15
    override fun getMaxCost(pLevel: Int): Int = 55

    private class EndDamage : Throwable()

    //iteration starts at 1
    fun damage(
        attacker: LivingEntity,
        targetEntity: LivingEntity,
        entitiesHit: MutableList<LivingEntity>,
        iteration: Int,
        event: LivingHurtEvent,
        extraFlags: List<String>,
        instrumentItem: InstrumentItem? = null
    ) {
        try {


            if (iteration > ServerConfig.ELECTRIC_MAX_JUMPS.get()) throw EndDamage()
            if (!targetEntity.isAlive) throw EndDamage()

            //Spawn Particles
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

            //Damage
            val damageFactor: Float = ServerConfig.ELECTRIC_DAMAGE_RETURNS.get() / iteration
            val damage = event.amount * damageFactor
            if (damage < 0.5) throw EndDamage()

            targetEntity.hurt(
                event.source,
                damage
            )

            instrumentItem?.attack(targetEntity)

            entitiesHit.add(targetEntity)

            //Spawn particle line
            val nextEntities: MutableList<LivingEntity> =
                ServerUtils.getNearbyLivingEntities(targetEntity, ServerConfig.ELECTRIC_RANGE.get())
                    .filter { it.isAlive && it !in entitiesHit }.toMutableList()

            //Look, this saves TWO booleans. TWO.
            //Otherwise, I'd have to include the original nearbyLiving from ModEvents, which would mean it's limited to what's near the ORIGINAL hit mob, and not the most recent.
            for (s in extraFlags) {
                when (s) {
                    "target = monster" -> {
                        nextEntities.removeIf { livingEntity: LivingEntity -> livingEntity !is Monster }
                        nextEntities.removeIf { livingEntity: LivingEntity -> livingEntity is Monster }
                    }

                    "attacker = monster" -> nextEntities.removeIf { livingEntity: LivingEntity -> livingEntity is Monster }
                }
            }

            if (nextEntities.isEmpty()) throw EndDamage()
            if (iteration + 1 > ServerConfig.ELECTRIC_MAX_JUMPS.get()) throw EndDamage()

            val nextEntity = ServerUtils.getNearestEntityInList(nextEntities, targetEntity) ?: throw EndDamage()

            ServerUtils.spawnElectricParticleLine(
                Vec3(targetEntity.x, targetEntity.y, targetEntity.z),
                Vec3(nextEntity.x, nextEntity.y, nextEntity.z),
                targetEntity.getLevel() as ServerLevel
            )

            ModScheduler.scheduleSynchronisedTask(ServerConfig.ELECTRIC_JUMPTIME.get()) {
                damage(
                    attacker,
                    nextEntity,
                    entitiesHit,
                    iteration + 1,
                    event,
                    extraFlags,
                    instrumentItem
                )
            }
        } catch (e: EndDamage) {
            currentElectricAttackers.remove(attacker)
        }
    }


}