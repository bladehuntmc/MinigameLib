package net.bladehunt.minigamelib.descriptor

import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.element.GameElement

class GameDescriptor<S : Game.Scope>(
    val name: String,
    val elements: List<GameElement<S>>
) {
}