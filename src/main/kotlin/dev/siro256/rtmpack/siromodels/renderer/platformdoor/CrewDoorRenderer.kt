package dev.siro256.rtmpack.siromodels.renderer.platformdoor

import dev.siro256.rtmpack.siromodels.block.platformdoor.CrewDoorTileEntity
import dev.siro256.rtmpack.siromodels.model.platformdoor.CrewDoorModel
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.RenderPass
import net.minecraft.tileentity.TileEntity

class CrewDoorRenderer : MachinePartsRenderer() {
    private val model by lazy { RenderDataManager.models[modelName] as CrewDoorModel }

    override fun render(tileEntity: TileEntity?, pass: RenderPass, tickProgression: Float) {
        if (pass != RenderPass.NORMAL) return

        if (tileEntity !is CrewDoorTileEntity?) return

        model.base.render(this)
        model.post.render(this)

        model.door.render(this)
        model.hinge.render(this)
        model.rod1.render(this)
        model.rod2.render(this)
        model.rod3.render(this)
        model.lockBar.render(this)
        model.handle.render(this)
    }
}
