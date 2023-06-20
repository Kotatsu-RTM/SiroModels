package dev.siro256.rtmpack.siromodels.model.platformdoor

import dev.siro256.rtmpack.siromodels.model.ModelRegistry
import jp.ngt.rtm.render.PartsRenderer

class WallModel(renderer: PartsRenderer<*, *>) : ModelRegistry(renderer) {
    val base = registerParts("base_1", "base_2", "base_3")
    val body = registerParts("body")
}
