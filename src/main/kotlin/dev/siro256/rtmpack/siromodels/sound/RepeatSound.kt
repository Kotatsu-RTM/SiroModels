package dev.siro256.rtmpack.siromodels.sound

import net.minecraft.client.audio.PositionedSound
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos

@Suppress("unused")
class RepeatSound(resourceLocation: ResourceLocation, category: SoundCategory, pos: BlockPos, volume: Float = 1.0F)
    : PositionedSound(resourceLocation, category) {
        init {
            xPosF = pos.x.toFloat()
            yPosF = pos.y.toFloat()
            zPosF = pos.z.toFloat()
            super.volume = volume
            repeat = true
        }
}
