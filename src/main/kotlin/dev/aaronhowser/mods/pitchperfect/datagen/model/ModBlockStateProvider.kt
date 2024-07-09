package dev.aaronhowser.mods.pitchperfect.datagen.model

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
    output: PackOutput,
    private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, PitchPerfect.ID, existingFileHelper) {

    override fun registerStatesAndModels() {
        conductor()
    }

    private fun conductor() {

        val block = ModBlocks.CONDUCTOR.get()

        getVariantBuilder(block)
            .forAllStates {
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .getExistingFile(modLoc("block/conductor"))
                    )
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/conductor"),
                existingFileHelper
            )
        )

    }

}