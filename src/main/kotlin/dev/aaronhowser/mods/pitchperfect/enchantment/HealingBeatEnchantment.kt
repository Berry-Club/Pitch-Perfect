package dev.aaronhowser.mods.pitchperfect.enchantment

import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.item.ItemStack

object HealingBeatEnchantment {

    fun trigger(user: LivingEntity, itemstack: ItemStack) {
        HealingBeatPulse(user, itemstack)
    }

    private class HealingBeatPulse(
        private val user: LivingEntity,
        itemStack: ItemStack
    ) {

        val mobsToHeal = findMobsToHeal()

        private fun pulse() {

            if (user.level().isClientSide) return

        }

        private fun findMobsToHeal(): List<LivingEntity> {
            val nearbyMobs = OtherUtil.getNearbyLivingEntities(user, ServerConfig.HEAL_RANGE.get())

            return nearbyMobs.filter { potentialMob ->

                val missingHealth = potentialMob.maxHealth > potentialMob.health
                if (!missingHealth) return@filter false

                val isBlacklisted = mobIsBlacklisted(potentialMob)
                if (isBlacklisted) return@filter false

                if (potentialMob is Monster) {
                    if (user is Monster) return@filter true
                    if (mobIsWhitelisted(potentialMob)) return@filter true
                } else {
                    // If the potential mob is not a monster but the user is, don't heal it
                    if (user is Monster) return@filter false
                }

                return@filter true
            }

        }

        fun mobIsWhitelisted(mob: LivingEntity): Boolean {
            val whitelist = ServerConfig.HEALING_BEAT_WHITELIST.get()

            val entityType = mob.type
            val entityRl = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)

            return whitelist.contains(entityRl.toString())
        }

        fun mobIsBlacklisted(mob: LivingEntity): Boolean {
            val blacklist = ServerConfig.HEALING_BEAT_BLACKLIST.get()

            val entityType = mob.type
            val entityRl = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)

            return blacklist.contains(entityRl.toString())
        }

    }

}