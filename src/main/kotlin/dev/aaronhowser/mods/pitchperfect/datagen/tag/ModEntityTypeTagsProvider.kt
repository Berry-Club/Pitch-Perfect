package dev.aaronhowser.mods.pitchperfect.datagen.tag

import dev.aaronhowser.mods.pitchperfect.ModTags
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
    pOutput: PackOutput,
    pProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(pOutput, pProvider, PitchPerfect.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(ModTags.HEALING_BEAT_WHITELIST)
            .add(EntityType.SNOW_GOLEM)

        this.tag(ModTags.HEALING_BEAT_BLACKLIST)
            .add(EntityType.ARMOR_STAND)

    }

}