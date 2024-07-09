package dev.aaronhowser.mods.pitchperfect.datagen.model

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.block.ConductorBlock
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
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
        composer()
    }

    private fun conductor() {

        val block = ModBlocks.CONDUCTOR.get()

        getVariantBuilder(block)
            .forAllStates {
                val half = it.getValue(ConductorBlock.HALF)
                val hasItem = it.getValue(ConductorBlock.FILLED)

                val modelLoc = if (half == DoubleBlockHalf.LOWER) {
                    modLoc(
                        if (hasItem) {
                            "block/conductor_filled"
                        } else {
                            "block/conductor"
                        }
                    )
                } else {
                    mcLoc("block/air")
                }

                val modelFile = models().getExistingFile(modelLoc)

                val facing: Direction = it.getValue(HorizontalDirectionalBlock.FACING)
                val yRotation = when (facing) {
                    Direction.NORTH -> 0
                    Direction.EAST -> 90
                    Direction.SOUTH -> 180
                    Direction.WEST -> 270
                    else -> throw IllegalStateException("Invalid facing direction")
                }

                ConfiguredModel
                    .builder()
                    .modelFile(modelFile)
                    .rotationY(yRotation)
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

    private fun composer() {
        simpleBlockWithItem(
            ModBlocks.COMPOSER.get(),
            cubeAll(ModBlocks.COMPOSER.get())
        )
    }

}