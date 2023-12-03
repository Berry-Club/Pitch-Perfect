package com.aaronhowser1.pitchperfect.utils

object CommonUtils {

    fun map(value: Float, min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (max2 - min2) * ((value - min1) / (max1 - min1))
    }

}