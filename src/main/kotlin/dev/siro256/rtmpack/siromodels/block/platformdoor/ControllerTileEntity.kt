package dev.siro256.rtmpack.siromodels.block.platformdoor

import jp.ngt.rtm.block.tileentity.TileEntityCrossingGate
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.util.math.AxisAlignedBB

class ControllerTileEntity: TileEntityCrossingGate() {
    private val renderAABB by lazy { AxisAlignedBB(pos).grow(20.0) }

    override fun getUpdatePacket() = SPacketUpdateTileEntity(pos, 1, writeToNBT(NBTTagCompound()))

    override fun getUpdateTag() = writeToNBT(NBTTagCompound())

    override fun handleUpdateTag(nbt: NBTTagCompound) {
        readFromNBT(nbt)
    }

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        readFromNBT(packet.nbtCompound)
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        return super.writeToNBT(nbt)
    }

    override fun getRenderBoundingBox() = renderAABB!!

    override fun update() {
        if (!world.isRemote) serverProcess() else clientProcess()
    }

    private fun serverProcess() {

    }

    private fun clientProcess() {

    }
}
