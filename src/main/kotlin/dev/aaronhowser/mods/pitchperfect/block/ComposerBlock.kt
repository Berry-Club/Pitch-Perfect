package dev.aaronhowser.mods.pitchperfect.block

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.screen.composer.ComposerScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

class ComposerBlock(
    private val properties: Properties = Properties.of()
        .strength(2f, 2f)
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
) : HorizontalDirectionalBlock(properties), EntityBlock {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
        )
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(FACING, pContext.horizontalDirection.opposite)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder)
        pBuilder.add(FACING)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    companion object {
        val CODEC: MapCodec<ComposerBlock> = simpleCodec(::ComposerBlock)
    }

    override fun codec(): MapCodec<ComposerBlock> {
        return CODEC
    }

    // Block Entity stuff

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return ComposerBlockEntity(pPos, pState)
    }

    override fun useWithoutItem(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHitResult: BlockHitResult
    ): InteractionResult {
        val blockEntity = pLevel.getBlockEntity(pPos)
                as? ComposerBlockEntity ?: return InteractionResult.CONSUME

        if (pPlayer is LocalPlayer) {
            val screen = ComposerScreen(blockEntity)
            Minecraft.getInstance().setScreen(screen)
        }

        super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult)

        return InteractionResult.CONSUME
    }

    override fun playerWillDestroy(pLevel: Level, pPos: BlockPos, pState: BlockState, pPlayer: Player): BlockState {
        val blockEntity = pLevel.getBlockEntity(pPos) as? ComposerBlockEntity

        if (!pLevel.isClientSide && pPlayer.isCreative && blockEntity != null) {

            val itemStack = this.asItem().defaultInstance
            itemStack.applyComponents(blockEntity.collectComponents())

            val itemEntity = ItemEntity(
                pLevel,
                pPos.x.toDouble() + 0.5,
                pPos.y.toDouble() + 0.5,
                pPos.z.toDouble() + 0.5,
                itemStack
            )

            itemEntity.setDefaultPickUpDelay()
            pLevel.addFreshEntity(itemEntity)

        }

        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer)
    }

    override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player
    ): ItemStack {
        val blockEntity = level.getBlockEntity(pos) as? ComposerBlockEntity
            ?: return super.getCloneItemStack(state, target, level, pos, player)

        val itemStack = this.asItem().defaultInstance
        itemStack.applyComponents(blockEntity.collectComponents())

        return itemStack
    }

    override fun setPlacedBy(
        pLevel: Level,
        pPos: BlockPos,
        pState: BlockState,
        pPlacer: LivingEntity?,
        pStack: ItemStack
    ) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack)

        val songComponent = pStack.get(ModDataComponents.COMPOSER_SONG_COMPONENT) ?: return
        val blockEntity = pLevel.getBlockEntity(pPos) as? ComposerBlockEntity ?: return

//        blockEntity.composerSongUuid = songComponent.composerSongUuid
//
//        blockEntity.setSong(songComponent.composerSong)
    }

    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pMovedByPiston: Boolean
    ) {
        if (pState.block != pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos)

            if (blockEntity is ComposerBlockEntity) {
                PitchPerfect.LOGGER.info("A Composer was broken! It had the uuid ${blockEntity.composerSongUuid}")
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: Item.TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag)
        val instrumentsComponent = Component.empty()

        val instruments = pStack.get(ModDataComponents.COMPOSER_SONG_COMPONENT)?.instruments ?: return
        for (screenInstrument in instruments) {
            val instrumentItem = ModItems.getFromSoundHolder(screenInstrument.noteBlockInstrument.soundEvent)

            val instrumentComponent = if (instrumentItem != null) {
                ModLanguageProvider.FontIcon.getIcon(instrumentItem.get())
            } else {
                Component.literal(screenInstrument.noteBlockInstrument.soundEvent.key.toString())
            }

            instrumentsComponent.append(instrumentComponent)
        }

        pTooltipComponents.add(instrumentsComponent)
    }

}