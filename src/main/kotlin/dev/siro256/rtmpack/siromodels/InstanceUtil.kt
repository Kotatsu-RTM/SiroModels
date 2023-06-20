package dev.siro256.rtmpack.siromodels

fun <T> Any.deepCopy(clazz: Class<*>, newInstance: T): T {
    if (clazz.superclass != null) deepCopy(clazz.superclass, newInstance)

    clazz.declaredFields.forEach {
        try {
            it.isAccessible = true
            it.set(newInstance, it.get(this))
        } catch (_: Exception) {
            //Do nothing
        }
    }

    return newInstance
}
