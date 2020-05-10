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

public class XylophoneItem extends Item {

    public XylophoneItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(PitchPerfect.PITCH_PERFECT));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack itemstack = player.getHeldItem(handIn);
        float pitch = player.rotationPitch;
        pitch = Math.abs(((pitch+90)/90)-2);
        worldIn.playSound(null,
                player.getPosX(), //poxX
                player.getPosY(), //posY
                player.getPosZ(), //posZ
                SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE,
                SoundCategory.PLAYERS,
                1.0F,
                pitch
        );
        if (ModConfig.DEBUG_PITCH.get()) {System.out.println(pitch);}
        return ActionResult.resultSuccess(itemstack);
    }
}
