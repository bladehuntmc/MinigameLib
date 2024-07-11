package net.bladehunt.minigamelib.store

import kotlin.reflect.KProperty
import net.bladehunt.minigamelib.event.PlayerLeaveGameEvent
import net.bladehunt.minigamelib.instance.InstancedGameInstance
import net.minestom.server.entity.Player
import net.minestom.server.tag.Tag
import net.minestom.server.tag.Taggable

class Store<A : Taggable, B>(val default: (A) -> B) {
    internal var tag: Tag<B>? = null

    private fun getOrCreateTag(taggable: A, property: KProperty<*>): Tag<B> {
        return tag
            ?: run {
                val new = Tag.Transient<B>(property.name)
                val value = default(taggable)
                tag = new

                taggable.setTag(new, value)

                new
            }
    }

    operator fun getValue(thisRef: A, property: KProperty<*>): B =
        thisRef.getTag(getOrCreateTag(thisRef, property))

    operator fun setValue(thisRef: A, property: KProperty<*>, value: B) {
        thisRef.setTag(getOrCreateTag(thisRef, property), value)
    }
}

fun <T : InstancedGameInstance<T>, B> T.store(default: (Player) -> B): Store<Player, B> {
    val store = Store(default)
    this.gameEventNode.addListener(PlayerLeaveGameEvent::class.java) { event ->
        val tag = store.tag ?: return@addListener
        event.getPlayer().removeTag(tag)
    }

    return store
}
