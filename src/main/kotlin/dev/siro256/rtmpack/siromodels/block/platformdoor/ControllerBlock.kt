package dev.siro256.rtmpack.siromodels.block.platformdoor

import dev.siro256.rtmpack.siromodels.Values
import jp.ngt.rtm.block.BlockCrossingGate
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

object ControllerBlock: BlockCrossingGate() {
    init {
        registryName = ResourceLocation(Values.MOD_ID, "platform_door_controller")
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = ControllerTileEntity().apply { world = worldIn }
}
