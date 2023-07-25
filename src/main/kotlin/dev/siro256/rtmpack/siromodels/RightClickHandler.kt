package dev.siro256.rtmpack.siromodels

import dev.siro256.rtmpack.siromodels.block.ornament.BlockLight
import dev.siro256.rtmpack.siromodels.block.ornament.TileEntityLight
import jp.ngt.rtm.RTMItem
import jp.ngt.rtm.item.ItemInstalledObject
import net.minecraft.util.math.MathHelper
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object RightClickHandler {
    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickBlock) {
        if (!event.side.isServer) return

        val itemStack = event.itemStack
        if (itemStack.item !is ItemInstalledObject) return

        if (
            ItemInstalledObject.IstlObjType.getType(itemStack.itemDamage) !=
            ItemInstalledObject.IstlObjType.FLUORESCENT
        ) return

        val state = (RTMItem.installedObject as ItemInstalledObject).getModelState(itemStack).writeToNBT()

        if (!state.getString("ResourceName").contains(Values.MOD_NAME)) return

        val world = event.world
        val face = event.face!!
        val pos = event.pos.offset(face)

        when (state.getString("ResourceName").removePrefix("!SiroModels_")) {
            "light_type1_2m" -> {
                val direction =
                    (MathHelper.floor(event.entityPlayer.rotationYaw * 4.0F / 360.0F + 0.5) and 3).let {
                        when (face.index) {
                            0 -> if (it != 0 && it != 2) 4 else 0
                            1 -> if (it != 0 && it != 2) 6 else 2
                            2 -> 1
                            3 -> 3
                            4 -> 5
                            else -> 7
                        }
                    }

                world.setBlockState(pos, BlockLight.defaultState)

                val tileEntity = world.getTileEntity(pos) as TileEntityLight
                tileEntity.dir = direction.toByte()
                tileEntity.resourceState.readFromNBT(state)
                tileEntity.updateResourceState()
            }
            else -> return
        }

        event.useItem = Event.Result.DENY
    }
}
