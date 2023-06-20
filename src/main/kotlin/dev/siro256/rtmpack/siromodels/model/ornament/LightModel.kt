package dev.siro256.rtmpack.siromodels.model.ornament

import dev.siro256.rtmpack.siromodels.model.ModelRegistry
import jp.ngt.rtm.render.PartsRenderer

class LightModel(renderer: PartsRenderer<*, *>) : ModelRegistry(renderer) {
    val body = registerParts("body")
    val light = registerParts("light")
}
