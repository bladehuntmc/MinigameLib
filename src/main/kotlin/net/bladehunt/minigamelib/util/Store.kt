package net.bladehunt.minigamelib.util

import kotlin.reflect.KProperty
import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.event.game.PlayerLeaveGameEvent
import net.minestom.server.entity.Player
import net.minestom.server.tag.Tag
import net.minestom.server.tag.Taggable

class Store<A : Taggable, B>(val default: (A) -> B) {
    internal var tag: Tag<B>? = null

    operator fun getValue(thisRef: A, property: KProperty<*>): B {
        if (tag == null) tag = Tag.Transient(property.name)

        var result = thisRef.getTag(tag!!)
        if (result == null) {
            val default = default(thisRef)
            thisRef.setTag(tag!!, default)
            result = default
        }

        return result
    }

    operator fun setValue(thisRef: A, property: KProperty<*>, value: B) {
        if (tag == null) tag = Tag.Transient(property.name)

        thisRef.setTag(tag!!, value)
    }
}

fun <T> Game.store(default: (Player) -> T): Store<Player, T> {
    val store = Store(default)
    eventNode().addListener(PlayerLeaveGameEvent::class.java) { event ->
        store.tag?.let { tag -> event.player.removeTag(tag) }
    }

    return store
}
