package dev.aaronhowser.mods.pitchperfect.util

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.resources.ResourceLocation

object OtherUtil {

    fun Float.map(min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (max2 - min2) * ((this - min1) / (max1 - min1))
    }

    fun modResource(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(PitchPerfect.ID, path)
    }

}