package dev.siro256.rtmpack.siromodels

import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.RenderPass

class CustomModelObject : ModelObject(null, null) {
    override fun renderWithTexture(entity: Any?, pass: RenderPass, tickProgression: Float) {
        renderer.render(entity, pass, tickProgression)
    }
}
