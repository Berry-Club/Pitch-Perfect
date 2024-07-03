package dev.aaronhowser.mods.pitchperfect.item

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.Item

class InstrumentItem(
    val sound: Holder.Reference<SoundEvent>
) : Item(
    Properties()
        .durability(100)
) {
}