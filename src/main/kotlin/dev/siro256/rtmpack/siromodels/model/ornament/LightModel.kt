package dev.siro256.rtmpack.siromodels.model.ornament

import com.github.kotatsu_rtm.kotatsulib.mc1_12_2.api.model.Model
import jp.ngt.rtm.render.ModelObject

class LightModel(modelObject: ModelObject) : Model(modelObject.model.groupObjects) {
    val body by lazy { generateDrawGroup("body") }
    val light by lazy { generateDrawGroup("light") }
}
