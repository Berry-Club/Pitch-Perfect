package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.config.ServerConfig
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraftforge.registries.ForgeRegistries

class HealingBeatEnchantment : Enchantment(
    Rarity.COMMON,
    ModEnchantments.INSTRUMENT,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {
    override fun getMinCost(pLevel: Int): Int = 1
    override fun getMaxCost(pLevel: Int): Int = 20

    fun getTargets(user: LivingEntity): List<LivingEntity> {
        val nearbyMobs: List<LivingEntity> =
            ServerUtils.getNearbyLivingEntities(user, user.boundingBox.getSize().toInt())
        return getMobsToHeal(nearbyMobs)
    }

    fun heal(target: LivingEntity) {
        target.heal(ServerConfig.HEAL_AMOUNT.get())
    }

    private fun isMonster(target: LivingEntity): Boolean {
        return target is Monster
    }


    private fun getMobsToHeal(livingEntityList: List<LivingEntity>): List<LivingEntity> {
        return livingEntityList.filter { mob: LivingEntity ->
            val canBeHealed = mob.health < mob.maxHealth
            if (!canBeHealed) return@filter false

            if (mobIsBlacklisted(mob)) return@filter false

            if (isMonster(mob) && !mobIsWhitelisted((mob))) return@filter false

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