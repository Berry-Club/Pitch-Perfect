package com.aaronhowser1.pitchperfect.utils

import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.level.Level

object ClientUtils {

    private val level: Level
        get() = Minecraft.getInstance().level!!

    fun spawnParticle(
        particleType: SimpleParticleType,
        x: Double,
        y: Double,
        z: Double,
        red: Float,
        green: Float,
        blue: Float
    ) {
        level.addParticle(
            particleType,
            x, y, z,
            red.toDouble(), green.toDouble(), blue
                .toDouble()
        )
    }

    fun spawnNote(pitch: Float, x: Double, y: Double, z: Double) {
        val noteColor = CommonUtils.map(pitch, 0.5f, 2f, 0f, 24f)
        spawnParticle(
            ParticleTypes.NOTE,
            x, y, z,
            noteColor / 24, 0f, 0f
        )
        if (false) //set true to debug
            println(
                ("Spawning particle:\nPosition: "
                        + x + "," + y + "," + z
                        + "\nPitch: " + pitch
                        + "\nColor: " + (noteColor / 24))
            )
    }

}