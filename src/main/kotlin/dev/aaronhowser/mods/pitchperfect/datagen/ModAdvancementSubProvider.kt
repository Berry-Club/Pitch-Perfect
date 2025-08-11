package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.datagen.language.ModAdvancementLang
import dev.aaronhowser.mods.pitchperfect.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.function.Consumer

class ModAdvancementSubProvider : AdvancementProvider.AdvancementGenerator {

	private lateinit var registries: HolderLookup.Provider
	private lateinit var saver: Consumer<AdvancementHolder>
	private lateinit var existingFileHelper: ExistingFileHelper

	private fun guide(string: String) = OtherUtil.modResource("guide/$string")

	private fun AdvancementHolder.add(): AdvancementHolder {
		this@ModAdvancementSubProvider.saver.accept(this)
		return this
	}

	override fun generate(
		registries: HolderLookup.Provider,
		saver: Consumer<AdvancementHolder>,
		existingFileHelper: ExistingFileHelper
	) {
		this.registries = registries
		this.saver = saver
		this.existingFileHelper = existingFileHelper

		val root = makeRoot(registries)

		Advancement.Builder.advancement()
			.parent(root)
			.display(
				Items.ZOMBIE_HEAD,
				ModAdvancementLang.HIT_MOB_TITLE.toComponent(),
				ModAdvancementLang.HIT_MOB_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"hit_mob",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.build(guide("hit_mob"))
			.add()

		val makeComposer =
			Advancement.Builder.advancement()
				.parent(root)
				.display(
					ModBlocks.COMPOSER.get(),
					ModAdvancementLang.MAKE_COMPOSER_TITLE.toComponent(),
					ModAdvancementLang.MAKE_COMPOSER_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"make_composer",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.COMPOSER.get())
				)
				.build(guide("make_composer"))
				.add()

		val makeConductor =
			Advancement.Builder.advancement()
				.parent(makeComposer)
				.display(
					ModBlocks.CONDUCTOR.get(),
					ModAdvancementLang.MAKE_CONDUCTOR_TITLE.toComponent(),
					ModAdvancementLang.MAKE_CONDUCTOR_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"make_conductor",
					InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CONDUCTOR.get())
				)
				.build(guide("make_conductor"))
				.add()

		Advancement.Builder.advancement()
			.parent(makeConductor)
			.display(
				ModBlocks.CONDUCTOR.get(),
				ModAdvancementLang.CONDUCTOR_COMPLEX_TITLE.toComponent(),
				ModAdvancementLang.CONDUCTOR_COMPLEX_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"conductor_complex_song",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.build(guide("conductor_complex_song"))
			.add()

		val enchantInstrument =
			Advancement.Builder.advancement()
				.parent(root)
				.display(
					Items.ENCHANTING_TABLE,
					ModAdvancementLang.ENCHANT_INSTRUMENT_TITLE.toComponent(),
					ModAdvancementLang.ENCHANT_INSTRUMENT_DESC.toComponent(),
					null,
					AdvancementType.TASK,
					true, true, false
				)
				.addCriterion(
					"enchant_instrument",
					CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
				)
				.build(guide("enchant_instrument"))
				.add()

		Advancement.Builder.advancement()
			.parent(enchantInstrument)
			.display(
				Items.LIGHTNING_ROD,
				ModAdvancementLang.AND_HIS_MUSIC_TITLE.toComponent(),
				ModAdvancementLang.AND_HIS_MUSIC_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"and_his_music_was_electric",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.build(guide("and_his_music_was_electric"))
			.add()

		Advancement.Builder.advancement()
			.parent(enchantInstrument)
			.display(
				Items.GOLDEN_APPLE,
				ModAdvancementLang.HEALING_BEAT_TITLE.toComponent(),
				ModAdvancementLang.HEALING_BEAT_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"healing_beat",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.build(guide("healing_beat"))
			.add()

		Advancement.Builder.advancement()
			.parent(enchantInstrument)
			.display(
				Items.TNT,
				ModAdvancementLang.BWAAAP_TITLE.toComponent(),
				ModAdvancementLang.BWAAAP_DESC.toComponent(),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addCriterion(
				"bwaaap",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
			.build(guide("bwaaap"))
			.add()

	}

	private fun makeRoot(registries: HolderLookup.Provider): AdvancementHolder {
		val rootBuilder =
			Advancement.Builder.advancement()
				.display(
					ModItems.BANJO.get(),
					Component.literal("Pitch Perfect"),
					ModAdvancementLang.ROOT_DESC.toComponent(),
					ResourceLocation.withDefaultNamespace("textures/block/note_block.png"),
					AdvancementType.TASK,
					true,
					true,
					false
				)
				.requirements(AdvancementRequirements.Strategy.OR)

		rootBuilder.addCriterion(
			"has_instrument",
			InventoryChangeTrigger.TriggerInstance.hasItems(
				ItemPredicate.Builder.item()
					.of(ModItemTagsProvider.INSTRUMENTS)
					.build()
			)
		)

		return rootBuilder
			.build(guide("root"))
			.add()
	}
}