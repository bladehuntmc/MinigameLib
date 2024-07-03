package net.bladehunt.minigamelib.element

import net.bladehunt.minigamelib.instance.GameInstance
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

abstract class GameElement<T : GameInstance<T>> {
    val elementEventNode: EventNode<Event> = EventNode.all("Element")

    context(T)
    abstract suspend fun run()
}
