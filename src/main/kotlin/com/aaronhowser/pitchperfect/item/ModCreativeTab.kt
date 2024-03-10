package com.aaronhowser.pitchperfect.item

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object ModCreativeTab {
    val MOD_TAB: CreativeModeTab = object : CreativeModeTab("pitchperfect") {
        override fun makeIcon(): ItemStack {
            return ItemStack(ModItems.BASS.get())
        }
    }
}