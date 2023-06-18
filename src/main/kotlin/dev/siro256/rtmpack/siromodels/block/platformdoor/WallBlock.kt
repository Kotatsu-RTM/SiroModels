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

object WallBlock: BlockCrossingGate() {
    private val oneMeterDoor = AxisAlignedBB(0.0, 0.0, 0.5, 1.0, 1.35, 0.8)
    private val twoMeterDoor = AxisAlignedBB(-1.0, 0.0, 0.5, 1.0, 1.35, 0.8)

    init {
        registryName = ResourceLocation(Values.MOD_ID, "platform_door_wall")
    }

    override fun createNewTileEntity(worldIn: World, par2: Int) = WallTileEntity().apply { world = worldIn }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getSelectedBoundingBox(state: IBlockState, world: World, pos: BlockPos): AxisAlignedBB =
        @Suppress("DEPRECATION")
        getBoundingBox(state, world, pos).offset(pos)

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getBoundingBox(state: IBlockState, world: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        val tileEntity = world.getTileEntity(pos)

        if (tileEntity !is WallTileEntity) return oneMeterDoor

        return when (tileEntity.getSize()) {
            Size.ONE_METER -> oneMeterDoor
            Size.TWO_METER -> twoMeterDoor
        }
    }

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
        @Suppress("DEPRECATION")
        getBoundingBox(state, world, pos).offset(pos).let {
            if (entityBox.intersects(it)) collidingBoxes.add(it)
        }
    }

    enum class Size {
        ONE_METER,
        TWO_METER;
    }
}
