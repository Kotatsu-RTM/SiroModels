package dev.siro256.rtmpack.siromodels.block.platformdoor

import dev.siro256.rtmpack.siromodels.Values
import jp.ngt.rtm.block.BlockCrossingGate
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

object CrewDoorBlock: BlockCrossingGate() {
    init {
        registryName = ResourceLocation(Values.MOD_ID, "platform_door_crew")
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = CrewDoorTileEntity().apply { world = worldIn }
}
