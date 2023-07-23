package dev.siro256.rtmpack.siromodels.block.ornament

import dev.siro256.rtmpack.siromodels.Values
import jp.ngt.rtm.block.BlockFluorescent
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

object BlockLight : BlockFluorescent() {
    init {
        registryName = ResourceLocation(Values.MOD_ID, "light")
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityLight().apply { world = worldIn }
}
