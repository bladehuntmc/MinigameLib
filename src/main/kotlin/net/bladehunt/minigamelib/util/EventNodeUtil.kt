package net.bladehunt.minigamelib.util

import net.bladehunt.minigamelib.InstancedGame
import net.bladehunt.minigamelib.element.GameElement
import net.bladehunt.minigamelib.event.element.ElementCompleteEvent
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent

context(InstancedGame, GameElement)
fun createElementInstanceEventNode(): EventNode<InstanceEvent> {
    val node = EventNode.type("current_element_instance_${uuid}", EventFilter.INSTANCE)

    instance.eventNode().addChild(node)
    this@GameElement.eventNode().addListener(ElementCompleteEvent::class.java) {
        instance.eventNode().removeChild(node)
    }

    return node
}
