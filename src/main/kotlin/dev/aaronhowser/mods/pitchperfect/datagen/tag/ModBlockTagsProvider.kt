package dev.aaronhowser.mods.pitchperfect.datagen.tag

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, PitchPerfect.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CONDUCTOR.get())
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.COMPOSER.get())

    }
}