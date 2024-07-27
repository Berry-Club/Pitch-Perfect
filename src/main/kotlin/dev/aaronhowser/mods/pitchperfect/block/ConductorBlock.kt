package dev.aaronhowser.mods.pitchperfect.block

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.pitchperfect.block.entity.ConductorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.*
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class ConductorBlock(
    private val properties: Properties = Properties.of()
        .strength(2f, 2f)
        .mapColor(MapColor.METAL)
        .sound(SoundType.METAL)
        .noOcclusion()
) : HorizontalDirectionalBlock(properties), EntityBlock {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(FILLED, false)
                .setValue(POWERED, false)
        )
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(FACING, pContext.horizontalDirection.opposite)
            .setValue(HALF, DoubleBlockHalf.LOWER)
            .setValue(FILLED, false)
            .setValue(POWERED, false)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder)
        pBuilder.add(FACING, HALF, FILLED, POWERED)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getOcclusionShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos): VoxelShape {
        val half = pState.getValue(HALF)
        val facing = pState.getValue(FACING)

        return Boxes.getShape(half, facing)
    }

    override fun getCollisionShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        val half = pState.getValue(HALF)

        return when (half) {
            DoubleBlockHalf.LOWER -> Boxes.Bottom.BASE_AND_STAND
            else -> Shapes.empty()
        }
    }

    override fun getShape(
        pState: BlockState,
        pLevel: BlockGetter,
        pPos: BlockPos,
        pContext: CollisionContext
    ): VoxelShape {
        val half = pState.getValue(HALF)
        val facing = pState.getValue(FACING)

        return Boxes.getShape(half, facing)
    }

    companion object {
        val HALF: EnumProperty<DoubleBlockHalf> = BlockStateProperties.DOUBLE_BLOCK_HALF
        val FILLED: BooleanProperty = BlockStateProperties.ENABLED
        val POWERED: BooleanProperty = BlockStateProperties.POWERED

        val CODEC: MapCodec<ConductorBlock> = simpleCodec(::ConductorBlock)
    }

    override fun codec(): MapCodec<ConductorBlock> {
        return CODEC
    }

    // Block Entity stuff

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
        return if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) ConductorBlockEntity(pPos, pState) else null
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
            if (blockEntity is ConductorBlockEntity) {
                blockEntity.dropDrops()
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
    }

    override fun useItemOn(
        pStack: ItemStack,
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHand: InteractionHand,
        pHitResult: BlockHitResult
    ): ItemInteractionResult {
        val mainPos = if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) pPos else pPos.below()
        val blockEntity =
            pLevel.getBlockEntity(mainPos) as? ConductorBlockEntity ?: return ItemInteractionResult.CONSUME

        blockEntity.playerClick(pPlayer)

        return ItemInteractionResult.CONSUME
    }

    override fun useWithoutItem(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHitResult: BlockHitResult
    ): InteractionResult {
        val mainPos = if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) pPos else pPos.below()
        val blockEntity = pLevel.getBlockEntity(mainPos) as? ConductorBlockEntity ?: return InteractionResult.FAIL

        blockEntity.playerClick(pPlayer)

        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult)
    }

    override fun shouldCheckWeakPower(state: BlockState, level: SignalGetter, pos: BlockPos, side: Direction): Boolean {
        return true
    }

    override fun spawnDestroyParticles(pLevel: Level, pPlayer: Player, pPos: BlockPos, pState: BlockState) {
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            super.spawnDestroyParticles(pLevel, pPlayer, pPos, pState)
            return
        }

        val posBelow = pPos.below()
        val stateBelow = pLevel.getBlockState(posBelow)

        super.spawnDestroyParticles(pLevel, pPlayer, posBelow, stateBelow)
    }

    override fun neighborChanged(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNeighborBlock: Block,
        pNeighborPos: BlockPos,
        pMovedByPiston: Boolean
    ) {
        val mainPos = if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) pPos else pPos.below()

        val isPowered = pLevel.hasNeighborSignal(pPos)

        if (isPowered == pState.getValue(POWERED)) return

        pLevel.setBlock(
            mainPos,
            pState.setValue(POWERED, isPowered),
            1 or 2
        )

        if (isPowered) {
            val blockEntity = pLevel.getBlockEntity(mainPos) as? ConductorBlockEntity ?: return
            blockEntity.redstonePulse()
        }
    }

    override fun canConnectRedstone(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        direction: Direction?
    ): Boolean {
        return true
    }

    // Multiblock functions:

    override fun canSurvive(pState: BlockState, pLevel: LevelReader, pPos: BlockPos): Boolean {
        val posBelow = pPos.below()
        val stateBelow = pLevel.getBlockState(posBelow)
        val stateAbove = pLevel.getBlockState(pPos.above())

        return if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            stateBelow.isFaceSturdy(pLevel, posBelow, Direction.UP) && (stateAbove.isAir || stateAbove.block == this)
        } else {
            stateBelow.block == this
        }
    }

    override fun destroy(pLevel: LevelAccessor, pPos: BlockPos, pState: BlockState) {
        val half = pState.getValue(HALF)

        val otherHalfPos = when (half) {
            DoubleBlockHalf.LOWER -> pPos.above()
            DoubleBlockHalf.UPPER -> pPos.below()
            else -> return
        }
        val otherHalfState = pLevel.getBlockState(otherHalfPos)

        if (otherHalfState.block == this) {
            pLevel.setBlock(
                otherHalfPos,
                Blocks.AIR.defaultBlockState(),
                1 or 2 or 32      // 1 = cause block update, 2 = send change to clients, 32 = neighbors dont drop items
            )
        }

        super.destroy(pLevel, pPos, pState)
    }

    override fun setPlacedBy(
        pLevel: Level,
        pPos: BlockPos,
        pState: BlockState,
        pPlacer: LivingEntity?,
        pStack: ItemStack
    ) {
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            pLevel.setBlock(
                pPos.above(),
                pState.setValue(HALF, DoubleBlockHalf.UPPER),
                3
            )
        }
    }

    private object Boxes {
        fun getShape(half: DoubleBlockHalf, facing: Direction): VoxelShape {
            val combinedShape = Shapes.or(
                Bottom.getFacingShape(facing),
                Top.getFacingShape(facing)
            )

            return when (half) {
                DoubleBlockHalf.LOWER -> combinedShape
                DoubleBlockHalf.UPPER -> combinedShape.move(0.0, -1.0, 0.0)
                else -> Shapes.empty()
            }
        }

        object Bottom {

            fun getFacingShape(facing: Direction): VoxelShape {
                return when (facing) {
                    Direction.NORTH -> SHAPE_N
                    Direction.SOUTH -> SHAPE_S
                    Direction.EAST -> SHAPE_E
                    Direction.WEST -> SHAPE_W
                    else -> SHAPE_N
                }
            }

            val BASE_AND_STAND: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND)

            val SHAPE_N: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_NS)
            val SHAPE_S: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_NS)
            val SHAPE_E: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_EW)
            val SHAPE_W: VoxelShape = Shapes.or(Parts.BASE, Parts.STAND, Parts.TRAY_EW)

            private object Parts {
                val STAND: VoxelShape = Block.box(
                    7.0, 1.0, 7.0,
                    7.0 + 2, 1.0 + 14.8, 7.0 + 2
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
            }

        }

        object Top {

            fun getFacingShape(facing: Direction): VoxelShape {
                return when (facing) {
                    Direction.NORTH -> Parts.BACK_N
                    Direction.SOUTH -> Parts.BACK_S
                    Direction.EAST -> Parts.BACK_E
                    Direction.WEST -> Parts.BACK_W
                    else -> Parts.BACK_N
                }
            }

            object Parts {
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
        }

    }

}