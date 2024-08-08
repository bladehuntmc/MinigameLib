package net.bladehunt.minigamelib.event.game

import net.bladehunt.minigamelib.Game

data class GameBeginEvent(override val game: Game) : GameEvent
