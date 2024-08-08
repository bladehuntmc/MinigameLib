package net.bladehunt.minigamelib.example.sneakoff

import java.util.*
import kotlinx.coroutines.delay
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.kotstom.extension.adventure.asComponent
import net.bladehunt.kotstom.extension.adventure.color
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.kotstom.extension.adventure.text
import net.bladehunt.minigamelib.InstancedGame
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.dsl.element
import net.bladehunt.minigamelib.dsl.gameDescriptor
import net.bladehunt.minigamelib.element.countdown
import net.bladehunt.minigamelib.example.lobbyInstance
import net.bladehunt.minigamelib.util.createElementInstanceEventNode
import net.bladehunt.minigamelib.util.store
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerStartSneakingEvent
import net.minestom.server.instance.Instance
import net.minestom.server.utils.NamespaceID

class SneakOff(instance: Instance) : InstancedGame(UUID.randomUUID(), instance) {
    override val id: NamespaceID = NamespaceID.from("example", "sneakoff")

    private var Player.sneakCount by store { 0 }

    override val descriptor: GameDescriptor = gameDescriptor {
        +element {
            countdown(requiredPlayerCount = 2, maxPlayerCount = 2, countdown = 5)
            sendMessage(text("Starting game!"))
        }

        +element {
            val eventNode = createElementInstanceEventNode()

            sendMessage(text("Sneak as many times as possible!"))
            eventNode.listen<PlayerStartSneakingEvent> {
                it.player.sendMessage("You sneaked")
                it.player.sneakCount++
            }

            repeat(10) { i ->
                sendMessage("${10 - i}s left".asComponent())
                delay(1000)
            }
        }
        +element {
            var message = "<-- Game Overview -->".color(NamedTextColor.YELLOW)
            players
                .sortedByDescending { it.sneakCount }
                .forEach { message = message.appendNewline() + "${it.username} - ${it.sneakCount}" }

            sendMessage(message)
        }
    }

    override fun Player.sendToFallback() {
        this.setInstance(lobbyInstance).join()
    }
}
