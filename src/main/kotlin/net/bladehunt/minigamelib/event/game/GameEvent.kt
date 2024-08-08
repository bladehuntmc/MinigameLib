package net.bladehunt.minigamelib.event.game

import net.bladehunt.minigamelib.Game
import net.minestom.server.event.Event

interface GameEvent : Event {
    val game: Game
}
