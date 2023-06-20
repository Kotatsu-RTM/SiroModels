package dev.siro256.rtmpack.siromodels

fun <T> Any.deepCopy(newInstance: T): T {
    if (this::class.java.superclass != null) this.deepCopy(newInstance)

    this::class.java.declaredFields.forEach {
        try {
            it.isAccessible = true
            it.set(newInstance, it.get(this))
        } catch (_: Exception) {
            //Do nothing
        }
    }

    return newInstance
}
