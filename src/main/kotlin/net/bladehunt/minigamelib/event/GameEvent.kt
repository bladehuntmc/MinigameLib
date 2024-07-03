package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.Game
import net.minestom.server.event.Event

interface GameEvent : Event {
    val game: Game<*>
}