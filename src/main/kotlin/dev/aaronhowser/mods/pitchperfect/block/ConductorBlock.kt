package dev.aaronhowser.mods.pitchperfect.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor

class ConductorBlock(
    private val properties: Properties = Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
) : HorizontalDirectionalBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
        return defaultBlockState().setValue(FACING, pContext.horizontalDirection.opposite)
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
        val CODEC: MapCodec<ConductorBlock> = simpleCodec(::ConductorBlock)
    }

    override fun codec(): MapCodec<ConductorBlock> {
        return CODEC
    }

}