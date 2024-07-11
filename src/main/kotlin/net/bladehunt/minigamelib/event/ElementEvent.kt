package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.element.GameElement
import net.bladehunt.minigamelib.instance.GameInstance

interface ElementEvent<T : GameInstance<T>> : GameInstanceEvent<T> {
    val element: GameElement<T>
}
