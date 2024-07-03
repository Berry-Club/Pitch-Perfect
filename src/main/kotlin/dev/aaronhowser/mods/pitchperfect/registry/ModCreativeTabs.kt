package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeTabs {

    val CREATIVE_TAB_REGISTRY: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, PitchPerfect.ID)

    val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        CREATIVE_TAB_REGISTRY.register("pitch_perfect", Supplier {
            CreativeModeTab.builder()
                .title(ModLanguageProvider.Misc.CREATIVE_TAB.toComponent())
                .icon { ModItems.BANJO.toStack() }
                .displayItems { a, b ->
                    b.acceptAll(ModItems.ITEM_REGISTRY.entries.map { (it as DeferredItem).toStack() })
                }
                .build()
        })

}