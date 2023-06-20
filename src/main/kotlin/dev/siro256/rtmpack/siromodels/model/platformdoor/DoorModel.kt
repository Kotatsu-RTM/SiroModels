package dev.siro256.rtmpack.siromodels.model.platformdoor

import com.github.kotatsu_rtm.kotatsulib.mc1_12_2.api.model.Model
import jp.ngt.rtm.render.ModelObject

class DoorModel(modelObject: ModelObject) : Model(modelObject.model.groupObjects) {
    val base by lazy { generateDrawGroup("base_1", "base_2", "base_3", "pipe") }
    val body by lazy { generateDrawGroup("body") }

    val doorLeft by lazy { generateDrawGroup("door_left") }
    val doorRight by lazy { generateDrawGroup("door_right") }

    val maintenancePanelLeft by lazy { generateDrawGroup("maintenance_panel_left") }
    val maintenancePanelRight by lazy { generateDrawGroup("maintenance_panel_right") }

    val controlPanel by lazy { generateDrawGroup("control_panel") }
    val nearIndicator by lazy { generateDrawGroup("near_indicator") }

    val lamp1 by lazy { generateDrawGroup("direction_lamp_1") }
    val lamp2 by lazy { generateDrawGroup("direction_lamp_2") }
    val lamp3 by lazy { generateDrawGroup("direction_lamp_3") }
    val lamp4 by lazy { generateDrawGroup("direction_lamp_4") }
    val lamp5 by lazy { generateDrawGroup("direction_lamp_5") }
    val lamp6 by lazy { generateDrawGroup("direction_lamp_6") }
    val lamps = listOf(lamp1, lamp2, lamp3, lamp4, lamp5, lamp6)
}
