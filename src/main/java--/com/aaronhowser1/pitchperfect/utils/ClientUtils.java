package com.aaronhowser1.pitchperfect.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;

public class ClientUtils {

    private static Level level = Minecraft.getInstance().level;

    public static void spawnParticle(SimpleParticleType particleType, double x, double y, double z, float red, float green, float blue) {
        level.addParticle(
                particleType,
                x, y, z,
                red, green, blue
        );
    }

    public static void spawnNote(float pitch, double x, double y, double z) {

        float noteColor = map(pitch, 0.5F, 2, 0, 24) ;
        spawnParticle(
                ParticleTypes.NOTE,
                x, y, z,
                noteColor/24,0,0
        );
        if (false) //set true to debug
            System.out.println("Spawning particle:\nPosition: "
                    +x+","+y+","+z
                    +"\nPitch: "+pitch
                    +"\nColor: "+noteColor/24
            );
    }

    private static float map(float value, float min1, float max1, float min2, float max2)
    {
        return min2 + (max2 - min2) * ((value - min1) / (max1 - min1));
    }

}
