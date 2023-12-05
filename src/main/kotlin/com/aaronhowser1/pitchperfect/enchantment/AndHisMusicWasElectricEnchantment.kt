package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.ModSounds
import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.event.ModScheduler
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.packet.ModPacketHandler
import com.aaronhowser1.pitchperfect.packet.SpawnElectricParticlePacket
import com.aaronhowser1.pitchperfect.utils.CommonUtils.hasEnchantment
import com.aaronhowser1.pitchperfect.utils.ServerUtils
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

    private val currentElectricArcs: MutableMap<DamageSource, MutableSet<LivingEntity>> = mutableMapOf()

    fun handleElectric(event: LivingHurtEvent) {

        if (event.isCanceled) return

        val target = event.entity
        val source: DamageSource = event.source

        val attacker: LivingEntity = source.entity as? LivingEntity ?: return

        if (source in currentElectricArcs.keys) return

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

        val entitiesHit: MutableList<LivingEntity> = arrayListOf(target, attacker)
        val nearbyLiving: MutableList<LivingEntity> =
            ServerUtils.getNearbyLivingEntities(
                target,
                ServerConfig.ELECTRIC_RANGE.get()
            ).filter { it.isAlive && it !in entitiesHit }.toMutableList()

        // God forgive me for what I've created
        val extraFlags: MutableList<ExtraFlags> = ArrayList()
        if (target is Monster) {
            nearbyLiving.removeIf { livingEntity: LivingEntity -> livingEntity !is Monster }
            extraFlags.add(ExtraFlags.TARGET_IS_MONSTER)
        }
        if (attacker is Monster) {
            nearbyLiving.removeIf { livingEntity: LivingEntity -> livingEntity is Monster }
            extraFlags.add(ExtraFlags.ATTACKER_IS_MONSTER)
        }

        if (nearbyLiving.isEmpty()) return

        if (attacker is Player) {
            electricItemStack.hurtAndBreak(1, attacker) { user: LivingEntity ->
                user.getLevel().playSound(
                    null,
                    attacker.blockPosition(),
                    ModSounds.GUITAR_SMASH.get(),
                    SoundSource.PLAYERS,
                    1f,
                    1f
                )
            }
        }

        val closestEntity = ServerUtils.getNearestEntityInList(nearbyLiving, target) ?: return
        ServerUtils.spawnElectricParticleLine(
            Vec3(target.x, target.y, target.z),
            Vec3(closestEntity.x, closestEntity.y, closestEntity.z),
            closestEntity.getLevel() as ServerLevel
        )

        currentElectricArcs[source] = entitiesHit.toMutableSet()

        //Wait for the particles to reach
        ModScheduler.scheduleSynchronisedTask(ServerConfig.ELECTRIC_JUMPTIME.get()) {
            damage(
                attacker,
                closestEntity,
                1,
                event,
                extraFlags,
                electricItemStack.item as InstrumentItem
            )
        }

    }


    private class EndDamage : Throwable()

    private enum class ExtraFlags {
        TARGET_IS_MONSTER,
        ATTACKER_IS_MONSTER
    }

    //iteration starts at 1
    private fun damage(
        attacker: LivingEntity,
        targetEntity: LivingEntity,
        iteration: Int,
        event: LivingHurtEvent,
        extraFlags: List<ExtraFlags>,
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
            val entitiesHit = currentElectricArcs[event.source] ?: throw EndDamage()

            //Damage
            val damage = event.amount * ServerConfig.ELECTRIC_DAMAGE_FACTOR.get().pow(iteration)
            if (damage < 0.5) throw EndDamage()

            targetEntity.hurt(
                event.source,
                damage
            )

            instrumentItem?.attack(targetEntity)

            entitiesHit.add(targetEntity)
            currentElectricArcs[event.source] = entitiesHit

            //Spawn particle line
            val nextEntities: MutableList<LivingEntity> =
                ServerUtils.getNearbyLivingEntities(targetEntity, ServerConfig.ELECTRIC_RANGE.get())
                    .filter { it.isAlive && it !in entitiesHit }.toMutableList()


            // If the attacker is not a monster, and a monster is attacked, aim only at monsters
            // If the attacker is not a monster, and a non-monster is attacked, aim at anything nearby
            // If the attacker is a monster, and a non-monster is attacked, aim only at non-monsters
            // If the attacker is a monster, and a monster is attacked, aim at anything nearby

            val attackerIsMonster = extraFlags.contains(ExtraFlags.ATTACKER_IS_MONSTER)
            val targetIsMonster = extraFlags.contains(ExtraFlags.TARGET_IS_MONSTER)

            if (!attackerIsMonster && targetIsMonster) {
                nextEntities.removeIf { it !is Monster }
            } else if (attackerIsMonster && !targetIsMonster) {
                nextEntities.removeIf { it is Monster }
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
                    iteration + 1,
                    event,
                    extraFlags,
                    instrumentItem
                )
            }
        } catch (e: EndDamage) {
            currentElectricArcs.remove(event.source)
        }
    }


}