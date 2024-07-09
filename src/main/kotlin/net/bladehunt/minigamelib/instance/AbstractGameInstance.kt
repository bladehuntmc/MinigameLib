package net.bladehunt.minigamelib.instance

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.element.GameElement
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

abstract class AbstractGameInstance<T : GameInstance<T>>(game: Game<T>) : GameInstance<T> {
    private var isComplete: Boolean = false

    private lateinit var job: Job

    override val gameEventNode: EventNode<Event> = EventNode.all("Minigame")

    final override val elements: Iterator<GameElement<T>> = game.descriptor.elements.iterator()

    override suspend fun start(coroutineScope: CoroutineScope) {
        coroutineScope.launch { execute() }
    }

    protected open suspend fun execute() {
        job =
            CoroutineScope(Dispatchers.Default).launch {
                @Suppress("UNCHECKED_CAST")
                with(this@AbstractGameInstance as T) {
                    elements.forEach {
                        gameEventNode.addChild(it.elementEventNode)
                        apply { it.run() }
                        gameEventNode.removeChild(it.elementEventNode)
                        if (isComplete) return@launch
                    }
                }
                isComplete = true
            }

        job.join()
    }

    override fun stop(force: Boolean) {
        if (isComplete) return
        isComplete = true
        if (force) job.cancel()
    }
}
