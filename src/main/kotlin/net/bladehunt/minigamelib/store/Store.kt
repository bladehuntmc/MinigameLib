package net.bladehunt.minigamelib.store

import java.util.*
import kotlin.reflect.KProperty
import net.minestom.server.entity.Player

class Store<T>(val default: (Player) -> T) {
    private val values: MutableMap<UUID, T> = hashMapOf()

    context(Player)
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return values.getOrPut(uuid) { default(this@Player) }
    }

    context(Player)
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        values[uuid] = value
    }
}
