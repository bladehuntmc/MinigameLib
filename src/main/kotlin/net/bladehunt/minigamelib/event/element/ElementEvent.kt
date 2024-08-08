package net.bladehunt.minigamelib.event.element

import net.bladehunt.minigamelib.element.GameElement
import net.bladehunt.minigamelib.event.game.GameEvent

interface ElementEvent : GameEvent {
    val element: GameElement
}
