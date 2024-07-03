package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.Game

interface GameScopeEvent<T : Game.Scope> : GameEvent {
    val scope: T

    override val game: Game<*>
        get() = scope.game
}