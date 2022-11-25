package com.aaronhowser1.pitchperfect.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ClientUtils {

    private static Level level = Minecraft.getInstance().level;

    public static void spawnParticle(SimpleParticleType particleType, double x, double y, double z, float red, float green, float blue) {
        level.addParticle(
                particleType,
                x, y, z,
                red, green, blue
        );
    }

//    public static void spawnParticleCloud(Vec3 origin, AABB cloudArea, SimpleParticleType particleType, String amountRange, float red, float green, float blue) {
//        //AmountRange is a string of format "1-5"
//        int particleAmountLowerBound = Integer.parseInt(amountRange.split("-")[0]);
//        int particleAmountUpperBound = Integer.parseInt(amountRange.split("-")[1]);
//
//        if (particleAmountLowerBound < particleAmountUpperBound) {
//            int randomAmount = (int) (Math.random() * (particleAmountUpperBound - particleAmountLowerBound) + particleAmountLowerBound);
//
//            for (int p = 1; p <= randomAmount; p++) {
//                float noteX = (float) (origin.x() + cloudArea.getXsize() * (Math.random() * 3 - 1.5));
//                float noteZ = (float) (origin.y() + cloudArea.getZsize() * (Math.random() * 3 - 1.5));
//                float noteY = (float) (origin.z() + cloudArea.getYsize() * (Math.random() * 1.5 - .75));
//
//                spawnParticle(particleType, noteX, noteY, noteZ, red, green, blue);
//            }
//        }
//
//    }

    public static void spawnNote(float pitch, double x, double y, double z) {

        float noteColor = map(pitch, 2, 0.5F, 0, 0.5F) + 0.75F;
        spawnParticle(
                ParticleTypes.NOTE,
                x, y, z,
                getColor(noteColor, "red"),
                getColor(noteColor, "green"),
                getColor(noteColor, "blue")
        );
        if (false) //set true to debug
            System.out.println("Spawning particle:\nPosition: "+
                    x+","+y+","+z+
                    "\nPitch: "+String.valueOf(pitch)+
                    "\nColor: "+
                    String.valueOf(getColor(noteColor, "red"))+","+
                    String.valueOf(getColor(noteColor, "green"))+","+
                    String.valueOf(getColor(noteColor, "blue"))
            );
    }
    public static float getColor(float pitch, String color) {
        return switch (color) {
            case "red" ->
                    Math.max(0.0F, Mth.sin((pitch + 0.0F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
            case "blue" ->
                    Math.max(0.0F, Mth.sin((pitch + 0.6666667F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
            case "green" ->
                    Math.max(0.0F, Mth.sin((pitch + 0.33333334F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F);
            default -> 0;
        };
    }

    private static float map(float value, float min1, float max1, float min2, float max2)
    {
        return min2 + (max2 - min2) * ((value - min1) / (max1 - min1));
    }

}
