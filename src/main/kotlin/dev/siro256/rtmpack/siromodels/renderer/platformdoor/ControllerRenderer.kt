package dev.siro256.rtmpack.siromodels.renderer.platformdoor

import dev.siro256.rtmpack.siromodels.block.platformdoor.ControllerTileEntity
import dev.siro256.rtmpack.siromodels.model.platformdoor.ControllerModel
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity

class ControllerRenderer: MachinePartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as ControllerModel }

    override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (pass != RenderPass.NORMAL) return

        if (tileEntity !is ControllerTileEntity?) return

        model.base.render(this)
        model.body.render(this)
        model.maintenancePanelLeft.render(this)
        model.maintenancePanelRight.render(this)
    }
}
