package com.aaronhowser1.pitchperfect.enchantment

import com.aaronhowser1.pitchperfect.PitchPerfect
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.EnchantmentCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModEnchantments {

    val INSTRUMENT = EnchantmentCategory.create(
        "INSTRUMENT"
    ) { item: Item? -> item is InstrumentItem }

    val ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PitchPerfect.MOD_ID)


}