package net.bladehunt.minigamelib.event.game

import net.bladehunt.minigamelib.Game

data class GameCompleteEvent(override val game: Game) : GameEvent
