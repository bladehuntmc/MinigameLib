package net.bladehunt.minigamelib.element

import kotlinx.coroutines.*
import net.bladehunt.minigamelib.event.PlayerJoinGameEvent
import net.bladehunt.minigamelib.event.PlayerLeaveGameEvent
import net.bladehunt.minigamelib.instance.GameInstance
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener

context(GameElement<*>, GameInstance<*>)
suspend inline fun countdown(
    requiredPlayerCount: Int,
    maxPlayerCount: Int,
    countdown: Int,
    crossinline onJoin:
        context(GameElement<*>, GameInstance<*>)
        (Player, Int) -> Unit =
        { player, count ->
            sendMessage(
                player.name
                    .append(Component.text(" joined the game. ", NamedTextColor.WHITE))
                    .append(Component.text("($count/$maxPlayerCount)", NamedTextColor.YELLOW)))
        },
    crossinline onLeave:
        context(GameElement<*>, GameInstance<*>)
        (Player, Int) -> Unit =
        { player, count ->
            sendMessage(
                player.name
                    .append(Component.text(" left the game. ", NamedTextColor.RED))
                    .append(Component.text("($count/$maxPlayerCount)", NamedTextColor.YELLOW)))
        },
    crossinline onCountdown: (Audience, Int) -> Unit = { audience, count ->
        audience.sendMessage(Component.text(count))
    }
) = coroutineScope {
    val future = CompletableDeferred<Unit>()
    var job: Job? = null
    var isCountingDown = false

    val joinListener =
        EventListener.of(PlayerJoinGameEvent::class.java) { event ->
            onJoin(this@GameElement, this@GameInstance, event.player, players.size)
            if (players.size >= requiredPlayerCount && !isCountingDown) {
                isCountingDown = true
                job = launch {
                    repeat(countdown) { index ->
                        onCountdown(this@GameInstance, countdown - index)
                        delay(1000)
                    }

                    future.complete(Unit)
                }
            }
        }

    val leaveListener =
        EventListener.of(PlayerLeaveGameEvent::class.java) { event ->
            onLeave(this@GameElement, this@GameInstance, event.player, players.size - 1)
            if (players.size - 1 < requiredPlayerCount && isCountingDown) {
                isCountingDown = false
                job?.cancel()
                job = null
            }
        }

    elementEventNode.addListener(joinListener)
    elementEventNode.addListener(leaveListener)

    future.await()

    elementEventNode.removeListener(joinListener)
    elementEventNode.removeListener(leaveListener)
}
