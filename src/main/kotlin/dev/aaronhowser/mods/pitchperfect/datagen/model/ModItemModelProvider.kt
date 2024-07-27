package dev.aaronhowser.mods.pitchperfect.datagen.model

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, PitchPerfect.ID, existingFileHelper) {

    override fun registerModels() {

        for (item in ModItems.ITEM_REGISTRY.entries) {
            if (item.get() is BlockItem) continue

            if (item === ModItems.DIDGERIDOO) {
                didgeridoo()
                continue
            }

            basicItem(item.get())
        }

    }

    private fun didgeridoo() {
        val didgeridooItem = ModItems.DIDGERIDOO.get()

        getBuilder(didgeridooItem.toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", "item/didgeridoo")
            .transforms()

            .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
            .scale(1.25f)
            .end()

            .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
            .scale(1.25f)
            .end()

            .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
            .scale(1.25f)
            .end()

            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
            .scale(1.25f)
            .end()

            .end()
    }

}