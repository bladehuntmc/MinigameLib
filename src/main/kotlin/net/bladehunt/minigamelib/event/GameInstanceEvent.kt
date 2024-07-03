package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.instance.GameInstance

interface GameInstanceEvent<T : GameInstance<T>> : GameEvent {
    val gameInstance: T

    override val game: Game<*>
        get() = gameInstance.game
}
