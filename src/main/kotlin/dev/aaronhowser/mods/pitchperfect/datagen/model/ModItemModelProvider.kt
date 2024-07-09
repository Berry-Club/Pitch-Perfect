package dev.aaronhowser.mods.pitchperfect.datagen.model

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, PitchPerfect.ID, existingFileHelper) {

    //TODO: Make Didgeridoo bigger
    override fun registerModels() {

        for (item in ModItems.ITEM_REGISTRY.entries) {
            if (item.get() is BlockItem) continue
            basicItem(item.get())
        }

    }

}