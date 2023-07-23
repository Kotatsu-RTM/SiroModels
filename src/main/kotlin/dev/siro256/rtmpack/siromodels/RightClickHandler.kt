package dev.siro256.rtmpack.siromodels

import dev.siro256.rtmpack.siromodels.block.ornament.BlockLight
import jp.ngt.rtm.RTMItem
import jp.ngt.rtm.item.ItemInstalledObject
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
        val pos = event.pos.offset(event.face!!)

        when (state.getString("ResourceName").removePrefix("!SiroModels_")) {
            "light_type1_2m" -> BlockLight.defaultState
            else -> return
        }.let { world.setBlockState(pos, it) }

        event.useItem = Event.Result.DENY
    }
}
