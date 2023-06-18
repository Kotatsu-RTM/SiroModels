package dev.siro256.rtmpack.siromodels.model.platformdoor

import dev.siro256.rtmpack.siromodels.model.ModelRegistry
import jp.ngt.rtm.render.PartsRenderer

class CrewDoorModel(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val base = registerParts("base_1", "base_2")
    val post = registerParts("post_left", "post_right")
    val door = registerParts("door")

    val hinge = registerParts("hinge")
    val rod1 = registerParts("rod_1")
    val rod2 = registerParts("rod_2")
    val rod3 = registerParts("rod_3")

    val lockBar = registerParts("lock_bar")
    val handle = registerParts("handle")
}
