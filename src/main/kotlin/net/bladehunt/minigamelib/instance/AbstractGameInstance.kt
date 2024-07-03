package net.bladehunt.minigamelib.instance

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select
import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.element.GameElement
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

abstract class AbstractGameInstance<T : GameInstance<T>>(final override val game: Game<T>) :
    GameInstance<T> {
    private var isComplete: Boolean = false
    private val closeSignal = CompletableDeferred<Unit>()

    override val gameEventNode: EventNode<Event> = EventNode.all("Minigame")

    final override val elements: Iterator<GameElement<T>> = game.descriptor.elements.iterator()

    override suspend fun start(): Unit = coroutineScope {
        select<Unit> {
            async {
                    with(this@AbstractGameInstance as T) {
                        elements.forEach {
                            gameEventNode.addChild(it.elementEventNode)
                            apply { it.run() }
                            gameEventNode.removeChild(it.elementEventNode)
                            if (isComplete) return@async
                        }
                    }
                }
                .onAwait

            closeSignal.onAwait
        }
        isComplete = true
    }

    override fun stop(force: Boolean) {
        if (isComplete) return
        isComplete = false
        if (force) closeSignal.complete(Unit)
    }
}
