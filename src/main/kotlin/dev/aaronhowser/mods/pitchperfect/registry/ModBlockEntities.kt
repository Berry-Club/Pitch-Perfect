package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.block.entity.ConductorBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlockEntities {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PitchPerfect.ID)

	val COMPOSER: DeferredHolder<BlockEntityType<*>, BlockEntityType<ComposerBlockEntity>> =
		register("composer", ::ComposerBlockEntity, ModBlocks.COMPOSER)

	val CONDUCTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<ConductorBlockEntity>> =
		register("conductor", ::ConductorBlockEntity, ModBlocks.CONDUCTOR)

	private fun <T : BlockEntity> register(
		name: String,
		builder: BlockEntityType.BlockEntitySupplier<out T>,
		vararg validBlocks: DeferredBlock<*>
	): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
		return BLOCK_ENTITY_REGISTRY.register(name, Supplier {
			BlockEntityType.Builder.of(
				builder,
				*validBlocks.map(DeferredBlock<*>::get).toTypedArray()
			).build(null)
		})
	}

}