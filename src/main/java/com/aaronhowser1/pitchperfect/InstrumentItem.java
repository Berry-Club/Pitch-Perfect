package com.aaronhowser1.pitchperfect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class InstrumentItem extends Item {

    public final Supplier<SoundEvent> sound;
    public InstrumentItem(Supplier<SoundEvent> s) {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(PitchPerfect.PITCH_PERFECT));
        sound = s;
    }
    //Thank you to LatvianModder for making this, since I flunked out of math
    public static float map(float value, float min1, float max1, float min2, float max2)
    {
        return min2 + (max2 - min2) * ((value - min1) / (max1 - min1));
    }
    //Also making all instruments one class
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack itemstack = player.getHeldItem(handIn);
        float pitch = player.rotationPitch;
        if (ModConfig.DEBUG_PITCH.get()) {System.out.println("before: "+pitch);}
        pitch = map(pitch,-90,90,2,0.5F);
        worldIn.playSound(null,
                player.getPosX(), //poxX
                player.getPosY(), //posY
                player.getPosZ(), //posZ
                sound.get(),
                SoundCategory.PLAYERS,
                ModConfig.INSTRUMENT_VOLUME.get(),
                pitch
        );
        if (ModConfig.DEBUG_PITCH.get()) {System.out.println("after: "+pitch);}
        return ActionResult.resultFail(itemstack);  //Stops it from flailing
    }
}
