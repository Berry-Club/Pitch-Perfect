package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.datagen.loot.ModBlockLootTableSubProvider
import dev.aaronhowser.mods.pitchperfect.datagen.model.ModBlockStateProvider
import dev.aaronhowser.mods.pitchperfect.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.pitchperfect.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.pitchperfect.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.pitchperfect.datagen.tag.ModItemTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(
	modid = PitchPerfect.ID,
	bus = EventBusSubscriber.Bus.MOD
)
object ModDataGen {

	@SubscribeEvent
	fun onGatherData(event: GatherDataEvent) {
		val generator: DataGenerator = event.generator
		val output: PackOutput = generator.packOutput
		val existingFileHelper: ExistingFileHelper = event.existingFileHelper
		val lookupProvider: CompletableFuture<HolderLookup.Provider> = event.lookupProvider

		generator.addProvider(event.includeClient(), ModLanguageProvider(output))

		generator.addProvider(
			event.includeClient(),
			ModBlockStateProvider(output, existingFileHelper)
		)

		generator.addProvider(
			event.includeClient(),
			ModItemModelProvider(output, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModRecipeProvider(output, lookupProvider)
		)

		val blockTagProvider = generator.addProvider(
			event.includeServer(),
			ModBlockTagsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModItemTagsProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			LootTableProvider(
				output,
				setOf(),
				listOf(
					LootTableProvider.SubProviderEntry(
						::ModBlockLootTableSubProvider,
						LootContextParamSets.BLOCK
					)
				),
				lookupProvider
			)
		)

		generator.addProvider(
			event.includeServer(),
			AdvancementProvider(
				output,
				lookupProvider,
				existingFileHelper,
				listOf(ModAdvancementSubProvider())
			)
		)

	}

}