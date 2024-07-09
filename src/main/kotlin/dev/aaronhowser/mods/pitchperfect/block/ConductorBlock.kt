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

    enum class Half {
        TOP,
        BOTTOM
    }

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

    override fun getOcclusionShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos): VoxelShape {
        return Boxes.getFacingShape(pState.getValue(FACING))
    }

    override fun getCollisionShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        return Boxes.BASE_AND_STAND
    }

    override fun getShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        return Boxes.getFacingShape(pState.getValue(FACING))
    }

    companion object {
        val CODEC: MapCodec<ConductorBlock> = simpleCodec(::ConductorBlock)
    }

    override fun codec(): MapCodec<ConductorBlock> {
        return CODEC
    }

    private object Boxes {

        fun getFacingShape(facing: Direction): VoxelShape {
            return when (facing) {
                Direction.NORTH -> SHAPE_N
                Direction.SOUTH -> SHAPE_S
                Direction.EAST -> SHAPE_E
                Direction.WEST -> SHAPE_W
                else -> SHAPE_N
            }
        }

        private object Parts {
            val STAND: VoxelShape = Block.box(
                7.0, 1.0, 7.0,
                7.0 + 2, 1.0 + 15, 7.0 + 2
            )

            val BASE: VoxelShape = Block.box(
                4.0, 0.0, 4.0,
                4.0 + 8, 0.0 + 1, 4.0 + 8
            )

            val TRAY_NS: VoxelShape = Block.box(
                3.0, 16.0, 5.0,
                3.0 + 10, 16.0 + 0.1, 5.0 + 4
            )

            val TRAY_EW: VoxelShape = Block.box(
                5.0, 16.0, 3.0,
                5.0 + 4, 16.0 + 0.1, 3.0 + 10
            )

            val BACK_N: VoxelShape = Shapes.or(
                Block.box(
                    3.0, 16.0, 9.0,
                    3.0 + 10, 16.0 + 4, 9.0 + 2
                ),
                Block.box(
                    3.0, 20.0, 10.0,
                    3.0 + 10, 20.0 + 4, 10.0 + 2
                )
            )

            val BACK_S: VoxelShape = Shapes.or(
                Block.box(
                    3.0, 16.0, 5.0,
                    3.0 + 10, 16.0 + 4, 5.0 + 2
                ),
                Block.box(
                    3.0, 20.0, 4.0,
                    3.0 + 10, 20.0 + 4, 4.0 + 2
                )
            )

            val BACK_E: VoxelShape = Shapes.or(
                Block.box(
                    5.0, 16.0, 3.0,
                    5.0 + 2, 16.0 + 4, 3.0 + 10
                ),
                Block.box(
                    4.0, 20.0, 3.0,
                    4.0 + 2, 20.0 + 4, 3.0 + 10
                )
            )

            val BACK_W: VoxelShape = Shapes.or(
                Block.box(
                    9.0, 16.0, 3.0,
                    9.0 + 2, 16.0 + 4, 3.0 + 10
                ),
                Block.box(
                    10.0, 20.0, 3.0,
                    10.0 + 2, 20.0 + 4, 3.0 + 10
                )
            )

        }

        val BASE_AND_STAND: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND)

        val SHAPE_N: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_NS, Parts.BACK_N)
        val SHAPE_S: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_NS, Parts.BACK_S)
        val SHAPE_E: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_EW, Parts.BACK_E)
        val SHAPE_W: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_EW, Parts.BACK_W)

    }

}