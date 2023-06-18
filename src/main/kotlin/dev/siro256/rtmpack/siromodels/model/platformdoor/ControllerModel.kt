package dev.siro256.rtmpack.siromodels.model.platformdoor

import dev.siro256.rtmpack.siromodels.model.ModelRegistry
import jp.ngt.rtm.render.PartsRenderer

class ControllerModel(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val base = registerParts("base_1", "base_2", "base_3", "pipe_1", "pipe_2")
    val body = registerParts("body")
    val maintenancePanelLeft = registerParts("maintenance_panel_left")
    val maintenancePanelRight = registerParts("maintenance_panel_right")
}
