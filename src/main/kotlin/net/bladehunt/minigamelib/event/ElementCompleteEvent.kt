package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.element.GameElement
import net.bladehunt.minigamelib.instance.GameInstance

data class ElementCompleteEvent<T : GameInstance<T>>(
    override val element: GameElement<T>,
    override val gameInstance: T
) : ElementEvent<T>
