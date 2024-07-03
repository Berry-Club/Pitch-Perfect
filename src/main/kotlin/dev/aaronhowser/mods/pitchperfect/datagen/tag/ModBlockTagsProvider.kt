package dev.aaronhowser.mods.pitchperfect.datagen.tag

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, PitchPerfect.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {
        // Why is this required for item tags???
    }
}