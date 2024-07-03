package net.bladehunt.minigamelib.example

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import net.bladehunt.kotstom.extension.adventure.plus
import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.dsl.element
import net.bladehunt.minigamelib.dsl.gameDescriptor
import net.minestom.server.instance.Instance

object ExampleGame : Game<ExampleGame.Scope>() {
    override val descriptor: GameDescriptor<Scope> = gameDescriptor {
        name = "Example Game"
        +element {
            println("Waiting for players...")
            coroutineScope {}
        }
        +element {
            delay(500)
            println("State: $exampleState")
        }
        +element { println("Game completed") }
    }

    override suspend fun start(scope: Scope) {
        with(scope) { descriptor.elements.forEach { it.apply { run() } } }
    }

    class Scope(override val instance: Instance) : InstanceScope() {
        override val game = ExampleGame

        var exampleState: String = "123"
    }
}
