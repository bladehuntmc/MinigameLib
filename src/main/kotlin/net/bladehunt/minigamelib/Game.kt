package net.bladehunt.minigamelib

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.event.GameEventFilter
import net.bladehunt.minigamelib.event.element.ElementBeginEvent
import net.bladehunt.minigamelib.event.element.ElementCompleteEvent
import net.bladehunt.minigamelib.event.game.GameBeginEvent
import net.bladehunt.minigamelib.event.game.GameCompleteEvent
import net.bladehunt.minigamelib.event.game.GameEvent
import net.kyori.adventure.audience.Audience
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.EventHandler
import net.minestom.server.event.EventNode
import net.minestom.server.utils.NamespaceID
import java.util.*

abstract class Game(val uuid: UUID) : Audience, EventHandler<GameEvent> {
    abstract val id: NamespaceID

    abstract val descriptor: GameDescriptor

    abstract val players: Collection<Player>

    private var isComplete: Boolean = false

    private lateinit var job: Job

    private val eventNode = MinecraftServer.getGlobalEventHandler().map(this, GameEventFilter)

    override fun eventNode(): EventNode<GameEvent> = eventNode

    suspend fun start() {
        val globalEventHandler = MinecraftServer.getGlobalEventHandler()

        globalEventHandler.call(GameBeginEvent(this))
        job =
            CoroutineScope(Dispatchers.Default).launch {
                descriptor.elements.forEach {
                    eventNode.addChild(it.eventNode())
                    globalEventHandler.call(ElementBeginEvent(this@Game, it))
                    it.run()
                    globalEventHandler.call(ElementCompleteEvent(this@Game, it))
                    eventNode.removeChild(it.eventNode())
                    if (isComplete) return@launch
                }
            }

        globalEventHandler.call(GameCompleteEvent(this))

        GameManager.unregister(this)

        job.join()

        isComplete = true
    }

    fun stop(force: Boolean = false) {
        if (isComplete) return
        isComplete = true
        if (force) job.cancel()
    }

    abstract fun addPlayer(player: Player)

    abstract fun removePlayer(player: Player)
}
