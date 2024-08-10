package net.bladehunt.minigamelib

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.event.GameEventFilter
import net.bladehunt.minigamelib.event.element.ElementBeginEvent
import net.bladehunt.minigamelib.event.element.ElementCompleteEvent
import net.bladehunt.minigamelib.event.game.*
import net.kyori.adventure.audience.Audience
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.EventHandler
import net.minestom.server.event.EventNode
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID

internal val GAME_TAG = Tag.Transient<Game>("game")

abstract class Game(val uuid: UUID) : Audience, EventHandler<GameEvent> {
    var canJoin: Boolean = false

    abstract val id: NamespaceID

    abstract val descriptor: GameDescriptor

    abstract val players: Collection<Player>

    private var isComplete: Boolean = false

    private lateinit var job: Job

    private val eventNode = MinecraftServer.getGlobalEventHandler().map(this, GameEventFilter)

    override fun eventNode(): EventNode<GameEvent> = eventNode

    private val hasStarted = AtomicBoolean(false)

    fun run(context: CoroutineContext = Dispatchers.Default) {
        check(!hasStarted.get()) { "This game has already started." }

        hasStarted.set(true)

        val globalEventHandler = MinecraftServer.getGlobalEventHandler()

        globalEventHandler.call(GameBeginEvent(this))

        eventNode.addListener(PlayerJoinGameEvent::class.java) { event ->
            event.player.setTag(GAME_TAG, this)
        }

        eventNode.addListener(PlayerLeaveGameEvent::class.java) { event ->
            event.player.removeTag(GAME_TAG)
        }

        job =
            CoroutineScope(context).launch {
                descriptor.elements.forEach {
                    eventNode.addChild(it.eventNode())
                    globalEventHandler.call(ElementBeginEvent(this@Game, it))
                    it.run()
                    globalEventHandler.call(ElementCompleteEvent(this@Game, it))
                    eventNode.removeChild(it.eventNode())
                    if (isComplete) return@launch
                }

                players.forEach { it.removeTag(GAME_TAG) }
                globalEventHandler.call(GameCompleteEvent(this@Game))

                GameManager.unregister(this@Game)

                isComplete = true
            }
    }

    fun stop(force: Boolean = false) {
        if (isComplete) return
        isComplete = true
        if (force) job.cancel()
    }

    abstract fun addPlayer(player: Player)

    abstract fun removePlayer(player: Player)
}
