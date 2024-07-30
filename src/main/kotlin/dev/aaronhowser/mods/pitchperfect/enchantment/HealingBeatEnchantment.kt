package dev.aaronhowser.mods.pitchperfect.enchantment

import dev.aaronhowser.mods.pitchperfect.ModTags
import dev.aaronhowser.mods.pitchperfect.config.ServerConfig
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnNotePacket
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object HealingBeatEnchantment {

    fun trigger(user: LivingEntity, itemstack: ItemStack) {
        HealingBeatPulse(user, itemstack)
    }

    private class HealingBeatPulse(
        private val user: LivingEntity,
        private val itemStack: ItemStack
    ) {

        val soundEvent = InstrumentItem.getSoundEvent(itemStack)
        val mobsToHeal by lazy { findMobsToHeal() }

        init {
            pulse()
        }

        private fun pulse() {
            if (user.level().isClientSide) return
            if (mobsToHeal.isEmpty()) return
            if (soundEvent == null) return

            mobsToHeal.forEach { heal(it) }

            if (user is Player) {
                user.cooldowns.addCooldown(
                    itemStack.item,
                    Mth.ceil(ServerConfig.HEAL_COOLDOWN_PER.get() * mobsToHeal.size)
                )
            }
        }

        private fun heal(target: LivingEntity) {
            target.heal(ServerConfig.HEAL_AMOUNT.get().toFloat())

            ModPacketHandler.messageNearbyPlayers(
                SpawnNotePacket(
                    soundEvent!!.location,
                    user.lookAngle.y.toFloat(),
                    target.x,
                    target.eyeY,
                    target.z
                ),
                user.level() as ServerLevel,
                target.eyePosition,
                64.0
            )
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
            val entityType = mob.type

            return entityType.`is`(ModTags.HEALING_BEAT_WHITELIST)
        }

        fun mobIsBlacklisted(mob: LivingEntity): Boolean {
            val entityType = mob.type

            return entityType.`is`(ModTags.HEALING_BEAT_BLACKLIST)
        }

    }

}