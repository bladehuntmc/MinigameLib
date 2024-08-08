package net.bladehunt.minigamelib.element

import kotlinx.coroutines.*
import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.event.game.PlayerJoinGameEvent
import net.bladehunt.minigamelib.event.game.PlayerLeaveGameEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener

context(Game, GameElement)
suspend fun countdown(
    requiredPlayerCount: Int,
    maxPlayerCount: Int,
    countdown: Int,
    onJoin: (Player, Int) -> Unit = { player, count ->
        sendMessage(
            player.name
                .append(Component.text(" joined the game. ", NamedTextColor.WHITE))
                .append(Component.text("($count/$maxPlayerCount)", NamedTextColor.YELLOW)))
    },
    onLeave: (Player, Int) -> Unit = { player, count ->
        sendMessage(
            player.name
                .append(Component.text(" left the game. ", NamedTextColor.RED))
                .append(Component.text("($count/$maxPlayerCount)", NamedTextColor.YELLOW)))
    },
    onCountdown: (Int) -> Unit = { count -> sendMessage(Component.text(count)) }
) = coroutineScope {
    canJoin = true
    val future = CompletableDeferred<Unit>()
    var job: Job? = null
    var isCountingDown = false

    val joinListener =
        EventListener.of(PlayerJoinGameEvent::class.java) { event ->
            onJoin(event.player, players.size)
            if (players.size >= requiredPlayerCount && !isCountingDown) {
                isCountingDown = true
                canJoin = false
                job = launch {
                    repeat(countdown) { index ->
                        onCountdown(countdown - index)
                        delay(1000)
                    }

                    future.complete(Unit)
                }
            }
        }

    val leaveListener =
        EventListener.of(PlayerLeaveGameEvent::class.java) { event ->
            onLeave(event.player, players.size - 1)
            if (players.size - 1 < requiredPlayerCount && isCountingDown) {
                isCountingDown = false
                job?.cancel()
                job = null
                canJoin = true
            }
        }

    this@Game.eventNode().apply {
        addListener(joinListener)
        addListener(leaveListener)
    }

    future.await()

    canJoin = false

    this@Game.eventNode().apply {
        removeListener(joinListener)
        removeListener(leaveListener)
    }
}
