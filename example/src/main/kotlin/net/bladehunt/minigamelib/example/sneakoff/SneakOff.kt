package net.bladehunt.minigamelib.example.sneakoff

import kotlinx.coroutines.delay
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.extension.adventure.asComponent
import net.bladehunt.kotstom.extension.adventure.color
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.dsl.element
import net.bladehunt.minigamelib.dsl.gameDescriptor
import net.bladehunt.minigamelib.element.countdown
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.player.PlayerStartSneakingEvent

object SneakOff : Game<SneakOffInstance>() {
    override val descriptor: GameDescriptor<SneakOffInstance> = gameDescriptor {
        name = "Example Game"

        element {
            countdown(requiredPlayerCount = 2, maxPlayerCount = 2, countdown = 5)
            sendMessage("Starting game!".asComponent())
        }
        element {
            sendMessage("Do stuff...".asComponent())
            elementEventNode.listen<PlayerStartSneakingEvent> {
                it.player.sendMessage("You sneaked")
                it.player.sneakCount += 1
            }

            repeat(10) { i ->
                sendMessage("${10 - i}s left".asComponent())
                delay(1000)
            }
        }
        element {
            sendMessage(
                "Game Overview".color(NamedTextColor.YELLOW).appendNewline() +
                    "Players: ${players.joinToString { "${it.username} - ${it.sneakCount}" }}")
        }
    }
}
