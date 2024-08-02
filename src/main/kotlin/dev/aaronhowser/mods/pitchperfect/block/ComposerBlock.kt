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
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.phys.BlockHitResult

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

                val songWip = blockEntity.songWip
                if (songWip != null) {
                    PitchPerfect.LOGGER.info("A Composer with a song was broken!")
                    PitchPerfect.LOGGER.info(songWip.song.toString())
                }
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
    }

    override fun getDrops(pState: BlockState, pParams: LootParams.Builder): MutableList<ItemStack> {
        return super.getDrops(pState, pParams)
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

    override fun setPlacedBy(
        pLevel: Level,
        pPos: BlockPos,
        pState: BlockState,
        pPlacer: LivingEntity?,
        pStack: ItemStack
    ) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack)

        val songComponent = pStack.get(ModDataComponents.SONG_WIP_COMPONENT) ?: return
        val blockEntity = pLevel.getBlockEntity(pPos) as? ComposerBlockEntity ?: return

        blockEntity.setSong(songComponent.song)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: Item.TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag)

        pTooltipComponents.addAll(
            1,
            listOf(
                ModLanguageProvider.FontIcon.getIcon(ModItems.BASS.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.BASS_DRUM.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.BANJO.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.BIT.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.CHIMES.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.COW_BELL.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.DIDGERIDOO.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.ELECTRIC_PIANO.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.FLUTE.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.GLOCKENSPIEL.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.GUITAR.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.HARP.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.SNARE_DRUM.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.STICKS.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.VIBRAPHONE.get()),
                ModLanguageProvider.FontIcon.getIcon(ModItems.XYLOPHONE.get()),
            )
        )
    }

}