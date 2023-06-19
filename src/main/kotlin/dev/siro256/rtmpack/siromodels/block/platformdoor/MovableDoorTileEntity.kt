package dev.siro256.rtmpack.siromodels.block.platformdoor

import dev.siro256.rtmpack.siromodels.sound.RepeatSound
import dev.siro256.rtmpack.siromodels.sound.Sounds
import jp.ngt.rtm.block.tileentity.TileEntityCrossingGate
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB

class MovableDoorTileEntity: TileEntityCrossingGate() {
    private val renderAABB by lazy { AxisAlignedBB(pos).grow(20.0) }

    private val playingSounds = mutableListOf<ISound>()

    var detectError = false
        private set

    var doorOpeningLeft = 1.0F
        private set(value) {
            field = value.coerceIn(0.0F, 1.0F)
        }
    var doorOpeningRight = 1.0F
        private set(value) {
            field = value.coerceIn(0.0F, 1.0F)
        }

    override fun getUpdateTag() = writeToNBT(NBTTagCompound())

    override fun handleUpdateTag(nbt: NBTTagCompound) {
        readFromNBT(nbt)
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity = SPacketUpdateTileEntity(pos, 0, updateTag)

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        readFromNBT(packet.nbtCompound)
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        if (nbt.hasKey("detectError")) detectError = nbt.getBoolean("detectError")
        if (nbt.hasKey("doorOpenLeft")) doorOpeningLeft = nbt.getFloat("doorOpeningLeft")
        if (nbt.hasKey("doorOpenRight")) doorOpeningRight = nbt.getFloat("doorOpeningRight")

        super.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        nbt.apply {
            setBoolean("detectError", detectError)
            setFloat("doorOpeningLeft", doorOpeningLeft)
            setFloat("doorOpeningRight", doorOpeningRight)
        }

        return super.writeToNBT(nbt)
    }

    override fun invalidate() {
        super.invalidate()
        playingSounds.forEach { Minecraft.getMinecraft().soundHandler.stopSound(it) }
    }

    override fun getRenderBoundingBox() = renderAABB!!

    override fun update() {
        if (!world.isRemote) serverProcess() else clientProcess()
    }

    private fun serverProcess() {
        val oldNBT = writeToNBT(NBTTagCompound())

        detectError = System.currentTimeMillis() / 5000 % 2 == 0L

        if (writeToNBT(NBTTagCompound()) != oldNBT)
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2)
    }

    private fun clientProcess() {
        manageErrorSound()
    }

    private fun manageErrorSound() {
        val sound = Sounds.Machine.Door.ERROR.soundName
        val handler = Minecraft.getMinecraft().soundHandler

        if (detectError) {
            if (playingSounds.find { it.soundLocation == sound } != null) return

            RepeatSound(sound, SoundCategory.BLOCKS, pos, 0.1F).let {
                playingSounds.add(it)
                handler.playSound(it)
            }
        } else {
            playingSounds.find { it.soundLocation == sound }?.let {
                handler.stopSound(it)
                playingSounds.remove(it)
            }
        }
    }
}
