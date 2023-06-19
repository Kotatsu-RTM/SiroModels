package dev.siro256.rtmpack.siromodels.block.platformdoor

import dev.siro256.rtmpack.siromodels.Values
import jp.ngt.rtm.block.BlockCrossingGate
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

object MovableDoorBlock: BlockCrossingGate() {
    init {
        registryName = ResourceLocation(Values.MOD_ID, "platform_door_movable")
    }

    private val fullyClosedSize = AxisAlignedBB(-2.0, 0.0, 0.5, 3.0, 1.35, 0.8)

    override fun createNewTileEntity(worldIn: World, meta: Int) = MovableDoorTileEntity().apply { world = worldIn }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getSelectedBoundingBox(state: IBlockState, world: World, pos: BlockPos): AxisAlignedBB =
        fullyClosedSize.offset(pos)

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getBoundingBox(state: IBlockState, world: IBlockAccess, pos: BlockPos) = fullyClosedSize

    @Suppress("OVERRIDE_DEPRECATION")
    override fun addCollisionBoxToList(
        state: IBlockState,
        world: World,
        pos: BlockPos,
        entityBox: AxisAlignedBB,
        collidingBoxes: MutableList<AxisAlignedBB>,
        entity: Entity?,
        isActualState: Boolean
    ) {
        listOf(
            AxisAlignedBB(-2.0, 0.0, 0.0, 3.0, 1.35, 1.0)
        )
            .map { it.offset(pos) }
            .forEach { if (entityBox.intersects(it)) collidingBoxes.add(it) }
    }
}
