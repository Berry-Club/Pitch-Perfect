package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.packet.ModPacketHandler
import com.aaronhowser1.pitchperfect.packet.SpawnElectricParticlePacket
import com.aaronhowser1.pitchperfect.utils.ModScheduler
import com.aaronhowser1.pitchperfect.utils.ServerUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.entity.living.LivingHurtEvent
import kotlin.math.min

object AndHisMusicWasElectricEnchant : Enchantment(
    Rarity.VERY_RARE,
    ModEnchantments.INSTRUMENT,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {

    override fun getMinCost(pLevel: Int): Int {
        return 15
    }

    override fun getMaxCost(pLevel: Int): Int {
        return 55
    }

    //iteration starts at 1
    fun damage(
        originEntity: LivingEntity?,
        targetEntity: LivingEntity?,
        entitiesHit: MutableList<LivingEntity?>,
        iteration: Int,
        event: LivingHurtEvent,
        extraFlags: List<String?>,
        vararg instrumentItems: InstrumentItem
    ) {
        if (iteration > ServerConfig.ELECTRIC_MAX_JUMPS.get()) return
        if (!targetEntity!!.isAlive) return

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
        targetEntity.hurt(
            event.source,
            event.amount * damageFactor
        )

        instrumentItems.firstOrNull()?.attack(targetEntity)

        entitiesHit.add(targetEntity)

        //Spawn particle line
        val nextEntities: MutableList<LivingEntity> =
            ServerUtils.getNearbyLivingEntities(targetEntity, ServerConfig.ELECTRIC_RANGE.get()).toMutableList()

        nextEntities.removeAll(entitiesHit.toSet())
        nextEntities.remove(targetEntity)

        //Look, this saves TWO booleans. TWO.
        //Otherwise, I'd have to include the original nearbyLiving from ModEvents, which would mean it's limited to what's near the ORIGINAL hit mob, and not the most recent.
        for (s in extraFlags) {
            when (s) {
                "target = monster" -> {
                    nextEntities.removeIf { livingEntity: LivingEntity? -> livingEntity !is Monster }
                    nextEntities.removeIf { livingEntity: LivingEntity? -> livingEntity is Monster }
                }

                "attacker = monster" -> nextEntities.removeIf { livingEntity: LivingEntity? -> livingEntity is Monster }
            }
        }

        if (nextEntities.isNotEmpty()) {
            if (iteration + 1 <= ServerConfig.ELECTRIC_MAX_JUMPS.get()) {
                val nextEntity = ServerUtils.getNearestEntity(nextEntities, targetEntity)
                if (nextEntity!!.isAlive) {
                    ServerUtils.spawnElectricParticleLine(
                        Vec3(targetEntity.x, targetEntity.y, targetEntity.z),
                        Vec3(nextEntity.x, nextEntity.y, nextEntity.z),
                        targetEntity.getLevel() as ServerLevel
                    )
                    ModScheduler.scheduleSynchronisedTask(
                        {
                            damage(
                                targetEntity,
                                nextEntity,
                                entitiesHit,
                                iteration + 1,
                                event,
                                extraFlags,
                                *instrumentItems
                            )
                        },
                        ServerConfig.ELECTRIC_JUMPTIME.get()
                    )
                }
            }
        }
    }

}