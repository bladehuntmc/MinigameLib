package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.event.game.GameEvent
import net.minestom.server.event.EventFilter

object GameEventFilter : EventFilter<GameEvent, Game> {
    override fun getHandler(event: GameEvent): Game = event.game

    override fun eventType(): Class<GameEvent> = GameEvent::class.java

    override fun handlerType(): Class<Game> = Game::class.java
}
