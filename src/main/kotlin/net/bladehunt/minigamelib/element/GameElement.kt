package net.bladehunt.minigamelib.element

import net.bladehunt.minigamelib.event.ElementEventFilter
import net.bladehunt.minigamelib.event.element.ElementEvent
import net.minestom.server.event.EventHandler
import net.minestom.server.event.EventNode

abstract class GameElement : EventHandler<ElementEvent> {
    private val eventNode: EventNode<ElementEvent> =
        EventNode.type("element_current", ElementEventFilter)

    override fun eventNode(): EventNode<ElementEvent> = eventNode

    abstract suspend fun run()
}
