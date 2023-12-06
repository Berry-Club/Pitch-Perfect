package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.config.ServerConfig
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.packet.ModPacketHandler
import com.aaronhowser1.pitchperfect.packet.SpawnNotePacket
import com.aaronhowser1.pitchperfect.utils.CommonUtils.asInstrument
import com.aaronhowser1.pitchperfect.utils.CommonUtils.ceil
import com.aaronhowser1.pitchperfect.utils.CommonUtils.isMonster
import com.aaronhowser1.pitchperfect.utils.ServerUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.ForgeRegistries

object HealingBeatEnchantment : Enchantment(
    Rarity.COMMON,
    ModEnchantments.INSTRUMENT,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {
    override fun getMinCost(pLevel: Int): Int = 1
    override fun getMaxCost(pLevel: Int): Int = 20

    fun heal(user: LivingEntity, itemStack: ItemStack) {
        HealingBeatPulse(itemStack, user)
    }

    private class HealingBeatPulse(
        itemStack: ItemStack,
        private val user: LivingEntity
    ) {

        val instrument: InstrumentItem? = itemStack.item.asInstrument()
        val mobsToHeal: List<LivingEntity>
            get() = getMobsToHeal()

        init {
            pulse()
        }

        private fun pulse() {
            if (user.level.isClientSide || instrument == null || mobsToHeal.isEmpty()) return

            mobsToHeal.forEach { heal(it) }

            if (user is Player) {
                user.cooldowns.addCooldown(
                    instrument,
                    (mobsToHeal.size * ServerConfig.HEAL_COOLDOWN_MULT.get()).ceil()
                )

            }
        }

        private fun heal(target: LivingEntity) {
            target.heal(ServerConfig.HEAL_AMOUNT.get())

            ModPacketHandler.messageNearbyPlayers(
                SpawnNotePacket(
                    instrument!!.sound.location,
                    user.lookAngle.y.toFloat(),
                    target.x,
                    target.eyeY,
                    target.z
                ),
                user.level as ServerLevel,
                Vec3(target.x, target.eyeY, target.z),
                64.0
            )
        }

        /**
         * @return A list of nearby mobs that can be healed.
         *
         * 1. Checks if the mob is at max health.
         * 2. Checks if the mob is blacklisted.
         * 3. If the mob is a monster, returns true if the mob is whitelisted or the user is a monster.
         * 4. If the mob is not a monster, returns false if the user IS a monster.
         */
        private fun getMobsToHeal(): List<LivingEntity> {

            val nearbyMobs = ServerUtils.getNearbyLivingEntities(user, user.boundingBox.size.ceil())

            return nearbyMobs.filter { possibleTarget: LivingEntity ->

                val canBeHealed = possibleTarget.health < possibleTarget.maxHealth
                if (!canBeHealed) return@filter false

                if (mobIsBlacklisted(possibleTarget)) return@filter false

                if (possibleTarget.isMonster()) {
                    if (user.isMonster()) return@filter true
                    if (mobIsWhitelisted(possibleTarget)) return@filter true
                } else {
                    if (user.isMonster()) return@filter false
                }

                return@filter true
            }
        }

        private fun mobIsWhitelisted(mob: LivingEntity): Boolean {
            val whitelist: List<String> = ServerConfig.HEALING_BEAT_WHITELIST.get()
            val entityType = mob.type
            val resourceLocation = ForgeRegistries.ENTITY_TYPES.getKey(entityType)!!
            return whitelist.contains(resourceLocation.toString())
        }

        private fun mobIsBlacklisted(mob: LivingEntity): Boolean {
            val blacklist: List<String> = ServerConfig.HEALING_BEAT_BLACKLIST.get()
            val entityType = mob.type
            val resourceLocation = ForgeRegistries.ENTITY_TYPES.getKey(entityType)!!
            return blacklist.contains(resourceLocation.toString())
        }
    }
}