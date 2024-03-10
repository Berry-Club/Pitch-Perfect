package com.aaronhowser.pitchperfect.enchantment

import com.aaronhowser.pitchperfect.PitchPerfect
import com.aaronhowser.pitchperfect.item.InstrumentItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEnchantments {

    val INSTRUMENT: EnchantmentCategory = EnchantmentCategory.create(
        "INSTRUMENT"
    ) { item: Item -> item is InstrumentItem }

    private val ENCHANTMENTS: DeferredRegister<Enchantment> =
        DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PitchPerfect.MOD_ID)

    private fun registerEnchant(name: String, enchantment: Enchantment): RegistryObject<Enchantment> =
        ENCHANTMENTS.register(name) { enchantment }

    val HEALING_BEAT = registerEnchant("healing_beat", HealingBeatEnchantment)
    val AND_HIS_MUSIC_WAS_ELECTRIC = registerEnchant("and_his_music_was_electric", AndHisMusicWasElectricEnchantment)
    val BWAAAP = registerEnchant("bwaaap", BwaaapEnchantment)

    fun register(eventBus: IEventBus) {
        ENCHANTMENTS.register(eventBus)
    }

}