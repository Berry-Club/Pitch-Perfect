package com.aaronhowser1.pitchperfect.items;

import com.aaronhowser1.pitchperfect.ModConfig;
import com.aaronhowser1.pitchperfect.PitchPerfect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BanjoItem extends Item {

    public BanjoItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(PitchPerfect.PITCH_PERFECT));
    }

    public static float map(float value, float min1, float max1, float min2, float max2)
    {
        return min2 + (max2 - min2) * ((value - min1) / (max1 - min1));
    }
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
                SoundEvents.BLOCK_NOTE_BLOCK_BANJO,
                SoundCategory.PLAYERS,
                1.0F,
                pitch
        );
        if (ModConfig.DEBUG_PITCH.get()) {System.out.println("after: "+pitch);}
        return ActionResult.resultFail(itemstack);  //Stops it from flailing
    }
}
