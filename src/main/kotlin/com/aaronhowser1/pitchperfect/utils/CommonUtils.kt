package com.aaronhowser1.pitchperfect.utils

import com.aaronhowser1.pitchperfect.PitchPerfect
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraftforge.registries.RegistryObject

object CommonUtils {

    fun Float.map(min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (max2 - min2) * ((this - min1) / (max1 - min1))
    }

    fun ItemStack.hasEnchantment(enchantment: Enchantment): Boolean =
        EnchantmentHelper.getItemEnchantmentLevel(enchantment, this) != 0

    fun Double.ceil(): Int = kotlin.math.ceil(this).toInt()
    fun Float.ceil(): Int = kotlin.math.ceil(this).toInt()

    fun Item.asInstrument(): InstrumentItem? = this as? InstrumentItem
    fun RegistryObject<Item>.asInstrument(): InstrumentItem? = this.get().asInstrument()

    fun LivingEntity.isMonster(): Boolean = this is Monster

    fun logIfError(condition: Boolean, message: () -> String) {
        if (!condition) PitchPerfect.LOGGER.error(message)
    }

}