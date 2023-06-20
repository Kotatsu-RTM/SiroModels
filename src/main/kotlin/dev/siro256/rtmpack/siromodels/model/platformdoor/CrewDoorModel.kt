package dev.siro256.rtmpack.siromodels.model.platformdoor

import com.github.kotatsu_rtm.kotatsulib.mc1_12_2.api.model.Model
import jp.ngt.rtm.render.ModelObject

class CrewDoorModel(modelObject: ModelObject) : Model(modelObject.model.groupObjects) {
    val base by lazy { generateDrawGroup("base_1", "base_2") }
    val post by lazy { generateDrawGroup("post_left", "post_right") }
    val door by lazy { generateDrawGroup("door") }

    val hinge by lazy { generateDrawGroup("hinge") }
    val rod1 by lazy { generateDrawGroup("rod_1") }
    val rod2 by lazy { generateDrawGroup("rod_2") }
    val rod3 by lazy { generateDrawGroup("rod_3") }

    val lockBar by lazy { generateDrawGroup("lock_bar") }
    val handle by lazy { generateDrawGroup("handle") }
}
