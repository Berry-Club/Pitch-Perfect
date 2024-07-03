package dev.aaronhowser.mods.pitchperfect.enchantment

import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level

object ModEnchantments {

    val healingBeatResourceKey: ResourceKey<Enchantment> =
        ResourceKey.create(Registries.ENCHANTMENT, OtherUtil.modResource("healing_beat"))
    val bwaaapResourceKey: ResourceKey<Enchantment> =
        ResourceKey.create(Registries.ENCHANTMENT, OtherUtil.modResource("bwaaap"))
    val andHisMusicWasElectricResourceKey: ResourceKey<Enchantment> =
        ResourceKey.create(Registries.ENCHANTMENT, OtherUtil.modResource("and_his_music_was_electric"))

    fun getEnchantHolder(level: Level, resourceKey: ResourceKey<Enchantment>): Holder.Reference<Enchantment> {
        return level.registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(resourceKey)
    }

}