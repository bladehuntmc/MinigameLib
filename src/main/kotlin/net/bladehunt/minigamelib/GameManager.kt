package net.bladehunt.minigamelib

import kotlinx.coroutines.Dispatchers
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

object GameManager : Iterable<Game> {
    private val games: ConcurrentHashMap<UUID, Game> = ConcurrentHashMap()

    fun <T : Game> getFirstGameByClass(clazz: Class<T>): T? {
        return games.values.firstOrNull { it::class.java == clazz } as T?
    }

    fun register(game: Game) {
        check(games.putIfAbsent(game.uuid, game) == null) { "Game is already registered" }
    }

    fun unregister(game: Game) {
        check(games.remove(game.uuid) != null) { "Game is not registered" }
    }

    fun isRegistered(game: Game) = games.containsKey(game.uuid)

    fun <T : Game> getOrCreateFirstJoinableGame(
        clazz: Class<T>,
        gameProvider: () -> T,
        context: CoroutineContext = Dispatchers.Default
    ): Game {
        return synchronized(games) {
            getFirstGameByClass(clazz)
                ?: gameProvider().also { game ->
                    register(game)
                    game.run(context)
                }
        }
    }

    inline fun <reified T : Game> getOrCreateFirstJoinableGame(
        context: CoroutineContext = Dispatchers.Default,
        noinline gameProvider: () -> T
    ) = getOrCreateFirstJoinableGame(T::class.java, gameProvider, context)

    override fun iterator(): Iterator<Game> = games.values.iterator()
}
