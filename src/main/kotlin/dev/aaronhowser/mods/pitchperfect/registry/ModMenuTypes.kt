package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.menu.ComposerMenu
import dev.aaronhowser.mods.pitchperfect.menu.ComposerScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModMenuTypes {

    val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
        DeferredRegister.create(BuiltInRegistries.MENU, PitchPerfect.ID)

    val COMPOSER =
        MENU_TYPE_REGISTRY.register("composer", Supplier {
            IMenuTypeExtension.create { id, inv, buf ->
                ComposerMenu(id, inv, buf)
            }
        })

    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(COMPOSER.get(), ::ComposerScreen)
    }

}