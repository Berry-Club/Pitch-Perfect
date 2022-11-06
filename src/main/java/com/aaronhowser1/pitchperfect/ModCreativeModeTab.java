package com.aaronhowser1.pitchperfect;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("pitchperfect") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BASS.get());
        }
    };
}