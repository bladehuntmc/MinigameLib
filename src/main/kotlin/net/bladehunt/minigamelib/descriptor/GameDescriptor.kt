package net.bladehunt.minigamelib.descriptor

import net.bladehunt.minigamelib.element.GameElement
import net.bladehunt.minigamelib.instance.GameInstance

data class GameDescriptor<T : GameInstance<T>>(val elements: List<GameElement<T>>)
