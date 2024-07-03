package net.bladehunt.minigamelib.instance

import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.element.GameElement
import net.kyori.adventure.audience.Audience
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

interface GameInstance<T : GameInstance<T>> : Audience {
    val game: Game<T>

    val gameEventNode: EventNode<Event>

    val elements: Iterator<GameElement<T>>

    val players: Collection<Player>

    suspend fun start()

    fun stop(force: Boolean = true)

    fun addPlayer(player: Player)

    fun removePlayer(player: Player)
}
