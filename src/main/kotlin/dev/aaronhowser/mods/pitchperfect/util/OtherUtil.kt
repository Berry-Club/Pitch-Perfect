package dev.aaronhowser.mods.pitchperfect.util

object OtherUtil {

    fun Float.map(min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (max2 - min2) * ((this - min1) / (max1 - min1))
    }

}