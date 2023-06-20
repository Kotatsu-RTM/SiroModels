package dev.siro256.rtmpack.siromodels.renderer.platformdoor

import dev.siro256.rtmpack.siromodels.model.platformdoor.WallModel
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity

class WallRenderer : MachinePartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as WallModel }

    override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (pass != RenderPass.NORMAL) return

        model.base.render(this)
        model.body.render(this)
    }
}
