package com.aaronhowser1.pitchperfect;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class InstrumentItem extends Item {

    public final SoundEvent sound;

    public InstrumentItem(SoundEvent s) {
        super(new Item.Properties()
                .stacksTo(1)
                .tab(ModCreativeModeTab.MOD_TAB)
        );
        sound = s;
    }

    public static float map(float value, float min1, float max1, float min2, float max2)
    {
        return min2 + (max2 - min2) * ((value - min1) / (max1 - min1));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        float pitch = player.getXRot();
        pitch = map(pitch, -90,90,2,0.5F);
        level.playLocalSound(
                player.getX(),
                player.getY(),
                player.getZ(),
                sound,
                SoundSource.PLAYERS,
                1, //Volume
                pitch,
                false
        );
        return InteractionResultHolder.fail(itemStack);
    }
}
