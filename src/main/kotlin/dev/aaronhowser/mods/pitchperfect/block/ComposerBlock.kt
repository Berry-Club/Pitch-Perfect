package dev.aaronhowser.mods.pitchperfect.block

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.BlockHitResult

class ComposerBlock(
    private val properties: Properties = Properties.of()
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
                blockEntity.dropDrops()
            }
        }
    }

    override fun useWithoutItem(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHitResult: BlockHitResult
    ): InteractionResult {
        val blockEntity = pLevel.getBlockEntity(pPos)
                as? ComposerBlockEntity ?: return InteractionResult.FAIL

        blockEntity.playerClick(pPlayer)

        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult)
    }

}