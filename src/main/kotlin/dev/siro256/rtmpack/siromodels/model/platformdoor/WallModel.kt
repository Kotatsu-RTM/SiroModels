package dev.siro256.rtmpack.siromodels.model.platformdoor

import com.github.kotatsu_rtm.kotatsulib.mc1_12_2.api.model.Model
import jp.ngt.rtm.render.ModelObject

class WallModel(modelObject: ModelObject) : Model(modelObject.model.groupObjects) {
    val base by lazy { generateDrawGroup("base_1", "base_2", "base_3") }
    val body by lazy { generateDrawGroup("body") }
}
