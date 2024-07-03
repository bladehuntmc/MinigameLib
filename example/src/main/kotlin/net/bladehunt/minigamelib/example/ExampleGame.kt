package net.bladehunt.minigamelib.example

import kotlinx.coroutines.delay
import net.bladehunt.kotstom.extension.adventure.asComponent
import net.bladehunt.kotstom.extension.adventure.color
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.dsl.element
import net.bladehunt.minigamelib.dsl.gameDescriptor
import net.bladehunt.minigamelib.element.countdown
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.instance.Instance

object ExampleGame : Game<ExampleGame.Scope>() {
    override val descriptor: GameDescriptor<Scope> = gameDescriptor {
        name = "Example Game"

        +element {
            countdown(requiredPlayerCount = 2, maxPlayerCount = 2, countdown = 5)
            sendMessage("Starting game!".asComponent())
        }
        +element {
            delay(2000)
            sendMessage("Do stuff...".asComponent())
            delay(2000)
        }
        +element {
            sendMessage(
                "Game Overview".color(NamedTextColor.YELLOW).appendNewline() +
                    "Players: ${players.joinToString { it.username }}")
        }
    }

    override suspend fun start(scope: Scope) {
        with(scope) { descriptor.elements.forEach { it.apply { run() } } }
    }

    class Scope(instance: Instance) : InstanceScope(instance) {
        override val game = ExampleGame

        var exampleState: String = "123"
    }
}
