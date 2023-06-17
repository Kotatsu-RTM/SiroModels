package dev.siro256.rtmpack.siromodels.sound

import dev.siro256.rtmpack.siromodels.Values
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent

object Sounds {
    object Machine {
        object Door {
            val ERROR = SoundEvent(ResourceLocation(Values.MOD_ID, "machine.door.error"))
                .apply { registryName = soundName }
        }
    }
}
