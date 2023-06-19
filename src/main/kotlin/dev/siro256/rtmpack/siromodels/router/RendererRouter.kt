package dev.siro256.rtmpack.siromodels.router

import dev.siro256.rtmpack.siromodels.model.ornament.LightModel
import dev.siro256.rtmpack.siromodels.model.platformdoor.ControllerModel
import dev.siro256.rtmpack.siromodels.model.platformdoor.CrewDoorModel
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.MovableDoorRenderer
import dev.siro256.rtmpack.siromodels.renderer.RenderDataManager
import dev.siro256.rtmpack.siromodels.model.platformdoor.DoorModel
import dev.siro256.rtmpack.siromodels.model.platformdoor.WallModel
import dev.siro256.rtmpack.siromodels.renderer.LightRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.ControllerRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.CrewDoorRenderer
import dev.siro256.rtmpack.siromodels.renderer.platformdoor.WallRenderer
import jp.ngt.rtm.render.MachinePartsRenderer
import jp.ngt.rtm.render.ModelObject
import jp.ngt.rtm.render.PartsRenderer

@Suppress("unused")
object RendererRouter {
    @JvmStatic
    fun init(renderer: PartsRenderer<*, *>, modelObject: ModelObject) {
        RenderDataManager.models[renderer.modelName] = when (renderer.modelName.removePrefix("!SiroModels_")) {
            "door_end_right", "door_end_left", "door_middle_right_front", "door_middle_left_front" -> {
                replaceRenderer(
                    modelObject,
                    deepCopy(MachinePartsRenderer::class.java, renderer, MovableDoorRenderer())
                )
                DoorModel(renderer)
            }
            "door_crew" -> {
                replaceRenderer(
                    modelObject,
                    deepCopy(MachinePartsRenderer::class.java, renderer, CrewDoorRenderer())
                )
                CrewDoorModel(renderer)
            }
            "door_controller" -> {
                replaceRenderer(
                    modelObject,
                    deepCopy(MachinePartsRenderer::class.java, renderer, ControllerRenderer())
                )
                ControllerModel(renderer)
            }
            "door_wall_1m", "door_wall_2m" -> {
                replaceRenderer(
                    modelObject,
                    deepCopy(MachinePartsRenderer::class.java, renderer, WallRenderer())
                )
                WallModel(renderer)
            }
            "light_type1_2m" -> {
                replaceRenderer(
                    modelObject,
                    deepCopy(MachinePartsRenderer::class.java, renderer, LightRenderer())
                )
                LightModel(renderer)
            }
            else -> throw IllegalStateException("Model ${renderer.modelName} isn't handled")
        }
    }

    private inline fun <reified T: PartsRenderer<*, *>> replaceRenderer(modelObject: ModelObject, newRenderer: T) {
        if (modelObject.renderer !is T)
            modelObject::class.java
                .getDeclaredField("renderer")
                .apply { isAccessible = true }
                .set(modelObject, newRenderer)
    }
    private fun <T> deepCopy(clazz: Class<*>, instance: Any, newInstance: T): T {
        if (clazz.superclass != null) deepCopy(clazz.superclass, instance, newInstance)

        clazz.declaredFields.forEach {
            try {
                it.isAccessible = true
                it.set(newInstance, it.get(instance))
            } catch (_: Exception) {
                //Do nothing
            }
        }

        return newInstance
    }

}
