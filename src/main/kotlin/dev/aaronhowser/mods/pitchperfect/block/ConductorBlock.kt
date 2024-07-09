package dev.aaronhowser.mods.pitchperfect.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

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

    override fun getOcclusionShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos): VoxelShape {
        return Boxes.ALL
    }

    override fun getCollisionShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        return Boxes.ALL
    }

    override fun getShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        return Boxes.ALL
    }

    companion object {
        val CODEC: MapCodec<ConductorBlock> = simpleCodec(::ConductorBlock)

        private object Boxes {

            val STAND: VoxelShape = Block.box(
                7.0, 1.0, 7.0,
                7.0 + 2, 1.0 + 15, 7.0 + 2
            )

            val BASE: VoxelShape = Block.box(
                4.0, 0.0, 4.0,
                4.0 + 8, 0.0 + 1, 4.0 + 8
            )

            val TRAY: VoxelShape = Block.box(
                3.0, 16.0, 5.0,
                3.0 + 10, 16.0 + 0.1, 5.0 + 4
            )

            val ALL: VoxelShape = Shapes.or(STAND, BASE, TRAY)

        }

    }

    override fun codec(): MapCodec<ConductorBlock> {
        return CODEC
    }

}