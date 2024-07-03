package dev.aaronhowser.mods.pitchperfect.enchantment

import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object ModEnchantments {

    val healingBeatResourceKey: ResourceKey<Enchantment> =
        ResourceKey.create(Registries.ENCHANTMENT, OtherUtil.modResource("healing_beat"))

}