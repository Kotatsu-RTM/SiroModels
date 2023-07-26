package dev.siro256.rtmpack.siromodels

import dev.siro256.rtmpack.siromodels.renderer.base.Renderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.RenderPass

class CustomModelObject : ModelObject(null, null) {
    override fun renderWithTexture(entity: Any?, pass: RenderPass, tickProgression: Float) {
        if (renderer !is Renderer) return
        renderer.render(entity, pass, tickProgression)
    }
}
