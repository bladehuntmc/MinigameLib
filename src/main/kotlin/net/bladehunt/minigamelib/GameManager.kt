package net.bladehunt.minigamelib

import java.util.*

object GameManager : Iterable<Game> {
    private val games: MutableMap<UUID, Game> = mutableMapOf()

    fun register(game: Game) {
        check(!games.contains(game.uuid)) { "Game is already registered" }
        games[game.uuid] = game
    }

    fun unregister(game: Game) {
        check(games.contains(game.uuid)) { "Game is not registered" }
        games.remove(game.uuid)
    }

    fun isRegistered(game: Game) = games.contains(game.uuid)

    override fun iterator(): Iterator<Game> = games.values.iterator()
}
