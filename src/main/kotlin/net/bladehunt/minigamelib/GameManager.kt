package net.bladehunt.minigamelib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.*
import kotlin.coroutines.CoroutineContext

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

    suspend inline fun <reified T : Game> getOrCreateFirstJoinableGame(
        context: CoroutineContext = Dispatchers.Default,
        block: () -> T
    ): Game {
        return filterIsInstance<T>().firstOrNull { it.canJoin }
            ?: block().also { game ->
                register(game)
                game.run()
                delay(50)
            }
    }

    override fun iterator(): Iterator<Game> = games.values.iterator()
}
