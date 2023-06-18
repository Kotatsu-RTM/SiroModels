package dev.siro256.rtmpack.siromodels.model.platformdoor

import dev.siro256.rtmpack.siromodels.model.ModelRegistry
import jp.ngt.rtm.render.PartsRenderer

class DoorModel(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
    val base = registerParts("base_1", "base_2", "base_3", "pipe")
    val body = registerParts("body")

    val doorLeft = registerParts("door_left")
    val doorRight = registerParts("door_right")

    val maintenancePanelLeft = registerParts("maintenance_panel_left")
    val maintenancePanelRight = registerParts("maintenance_panel_right")

    val controlPanel = registerParts("control_panel")
    val nearIndicator = registerParts("near_indicator")
    val directionLamp = DirectionLamp(renderer)

    class DirectionLamp(renderer: PartsRenderer<*, *>): ModelRegistry(renderer) {
        val lamp1 = registerParts("direction_lamp_1")
        val lamp2 = registerParts("direction_lamp_2")
        val lamp3 = registerParts("direction_lamp_3")
        val lamp4 = registerParts("direction_lamp_4")
        val lamp5 = registerParts("direction_lamp_5")
        val lamp6 = registerParts("direction_lamp_6")

        val list = listOf(
            lamp1,
            lamp2,
            lamp3,
            lamp4,
            lamp5,
            lamp6
        )
    }
}
