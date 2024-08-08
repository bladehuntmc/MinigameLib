package net.bladehunt.minigamelib.event.element

import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.element.GameElement

data class ElementCompleteEvent(override val game: Game, override val element: GameElement) :
    ElementEvent
