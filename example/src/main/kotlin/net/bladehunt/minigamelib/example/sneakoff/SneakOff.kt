package net.bladehunt.minigamelib.example.sneakoff

import kotlinx.coroutines.delay
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.extension.adventure.asComponent
import net.bladehunt.kotstom.extension.adventure.color
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.minigamelib.InstancedGame
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.dsl.element
import net.bladehunt.minigamelib.dsl.gameDescriptor
import net.bladehunt.minigamelib.element.countdown
import net.bladehunt.minigamelib.example.lobbyInstance
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.instance.Instance

object SneakOff : InstancedGame<SneakOffInstance> {
    override fun getFallback(instance: SneakOffInstance, player: Player): Instance = lobbyInstance

    override val descriptor: GameDescriptor<SneakOffInstance> = gameDescriptor {
        name = "Example Game"

        element {
            countdown(requiredPlayerCount = 2, maxPlayerCount = 2, countdown = 5)
            sendMessage("Starting game!".asComponent())
        }
        element {
            sendMessage("Sneak as many times as possible!".asComponent())
            elementEventNode.listen<PlayerStartSneakingEvent> {
                it.player.sendMessage("You sneaked")
                it.player.sneakCount++
            }

            repeat(10) { i ->
                sendMessage("${10 - i}s left".asComponent())
                delay(1000)
            }
        }
        element {
            var message = "<-- Game Overview -->".color(NamedTextColor.YELLOW)
            players
                .sortedByDescending { it.sneakCount }
                .forEach { message = message.appendNewline() + "${it.username} - ${it.sneakCount}" }

            sendMessage(message)
        }
    }
}
