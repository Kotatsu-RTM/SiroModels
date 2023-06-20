package dev.siro256.rtmpack.siromodels.block.platformdoor

import jp.ngt.rtm.block.tileentity.TileEntityCrossingGate
import net.minecraft.util.math.AxisAlignedBB

class WallTileEntity : TileEntityCrossingGate() {
    private val renderAABB by lazy { AxisAlignedBB(pos).grow(20.0) }

    fun getSize(): WallBlock.Size {
        return if (resourceState.resourceName.contains("2m")) WallBlock.Size.TWO_METER else WallBlock.Size.ONE_METER
    }

    override fun getRenderBoundingBox() = renderAABB!!

    override fun update() {
        //Do nothing
    }
}
