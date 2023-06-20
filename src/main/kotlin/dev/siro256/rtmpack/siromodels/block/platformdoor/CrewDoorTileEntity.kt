package dev.siro256.rtmpack.siromodels.block.platformdoor

import jp.ngt.rtm.block.tileentity.TileEntityCrossingGate
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.util.math.AxisAlignedBB

class CrewDoorTileEntity : TileEntityCrossingGate() {
    private val renderAABB by lazy { AxisAlignedBB(pos).grow(20.0) }

    var doorOpening = 1.0F
        private set(value) {
            field = value.coerceIn(0.0F, 1.0F)
        }

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

        doorOpening = nbt.getFloat("doorOpening")
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        nbt.apply {
            setFloat("doorOpening", doorOpening)
        }

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
