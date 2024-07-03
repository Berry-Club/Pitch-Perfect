package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.enchantment.AndHisMusicWasElectricEnchantment
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent

@EventBusSubscriber(
    modid = PitchPerfect.ID
)
object OtherEvents {

    @SubscribeEvent
    fun onLivingHurt(event: LivingHurtEvent) {
        AndHisMusicWasElectricEnchantment.handleElectric(event)
    }

}