package com.aaronhowser1.pitchperfect.event

import com.aaronhowser1.pitchperfect.ModSounds
import com.aaronhowser1.pitchperfect.PitchPerfect
import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import com.aaronhowser1.pitchperfect.enchantment.ModEnchantments
import com.aaronhowser1.pitchperfect.event.ModScheduler.scheduleSynchronisedTask
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.utils.CommonUtils.hasEnchantment
import com.aaronhowser1.pitchperfect.utils.ServerUtils.getNearbyLivingEntities
import com.aaronhowser1.pitchperfect.utils.ServerUtils.getNearestEntityInList
import com.aaronhowser1.pitchperfect.utils.ServerUtils.spawnElectricParticleLine
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Suppress("unused")
@Mod.EventBusSubscriber(modid = PitchPerfect.MOD_ID)
object ModEvents {

    @SubscribeEvent
    fun onLivingHurt(event: LivingHurtEvent) {
        val target = event.entity
        val attacker: LivingEntity = event.source.entity as? LivingEntity ?: return

        handleElectric(event)
        attacker.mainHandItem.item.apply {
            if (this is InstrumentItem) this.attack(target)
        }

    }

    private fun handleElectric(event: LivingHurtEvent) {

        val target = event.entity
        val attacker: LivingEntity = event.source.entity as? LivingEntity ?: return

        if (attacker in AndHisMusicWasElectricEnchantment.currentElectricAttackers) return

        //Sets to the first InstrumentItem that has the enchantment in your inventory, or stays null
        val electricItemStack: ItemStack? =
            if (attacker is Player) {
                attacker.inventory.items.firstOrNull { itemStack ->
                    itemStack.item is InstrumentItem && itemStack.hasEnchantment(ModEnchantments.AND_HIS_MUSIC_WAS_ELECTRIC.get())
                }
            } else {
                attacker.allSlots.firstOrNull { itemStack ->
                    itemStack.item is InstrumentItem && itemStack.hasEnchantment(ModEnchantments.AND_HIS_MUSIC_WAS_ELECTRIC.get())
                }
            }

        if (electricItemStack == null) return
        if (attacker.level.isClientSide) return

        val entitiesHit: MutableList<LivingEntity> = arrayListOf(target, attacker)
        val nearbyLiving: MutableList<LivingEntity> =
            getNearbyLivingEntities(
                target,
                ServerConfig.ELECTRIC_RANGE.get()
            ).filter { it.isAlive && it !in entitiesHit }.toMutableList()

        // God forgive me for what I've created
        val extraFlags: MutableList<String> = ArrayList()
        if (target is Monster) {
            nearbyLiving.removeIf { livingEntity: LivingEntity -> livingEntity !is Monster }
            extraFlags.add("target = monster")
        }
        if (attacker is Monster) {
            nearbyLiving.removeIf { livingEntity: LivingEntity -> livingEntity is Monster }
            extraFlags.add("attacker = monster")
        }

        if (nearbyLiving.isEmpty()) return

        electricItemStack.hurtAndBreak(1, attacker) { user: LivingEntity ->
            user.getLevel().playSound(
                null,
                attacker,
                ModSounds.GUITAR_SMASH.get(),
                SoundSource.PLAYERS,
                1f,
                1f
            )
        }

        val closestEntity = getNearestEntityInList(nearbyLiving, target) ?: return
        spawnElectricParticleLine(
            Vec3(target.x, target.y, target.z),
            Vec3(closestEntity.x, closestEntity.y, closestEntity.z),
            closestEntity.getLevel() as ServerLevel
        )

        AndHisMusicWasElectricEnchantment.currentElectricAttackers.add(attacker)

        //Wait for the particles to reach
        scheduleSynchronisedTask(ServerConfig.ELECTRIC_JUMPTIME.get()) {
            if (attacker is Player) {
                AndHisMusicWasElectricEnchantment.damage(
                    attacker,
                    closestEntity,
                    entitiesHit,
                    1,
                    event,
                    extraFlags,
                    electricItemStack.item as InstrumentItem
                )
            } else {
                AndHisMusicWasElectricEnchantment.damage(
                    attacker,
                    closestEntity,
                    entitiesHit,
                    1,
                    event,
                    extraFlags
                )
            }
        }

    }

    @SubscribeEvent
    fun serverTick(event: ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            ModScheduler.tick()
        }
    }
}
