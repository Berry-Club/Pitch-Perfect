package com.aaronhowser1.pitchperfect.event;

import com.aaronhowser1.pitchperfect.PitchPerfect;
import com.aaronhowser1.pitchperfect.item.InstrumentItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PitchPerfect.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof Player player) {
            if (player.getMainHandItem().getItem() instanceof InstrumentItem instrumentItem) {
                instrumentItem.attack(target);
            }
        }
    }
}
