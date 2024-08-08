package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.element.GameElement
import net.bladehunt.minigamelib.event.element.ElementEvent
import net.minestom.server.event.EventFilter

object ElementEventFilter : EventFilter<ElementEvent, GameElement> {
    override fun getHandler(event: ElementEvent): GameElement = event.element

    override fun eventType(): Class<ElementEvent> = ElementEvent::class.java

    override fun handlerType(): Class<GameElement> = GameElement::class.java
}
