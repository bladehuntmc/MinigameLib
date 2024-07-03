package net.bladehunt.minigamelib

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.event.PlayerJoinGameEvent
import net.bladehunt.minigamelib.event.PlayerLeaveGameEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance

abstract class Game<S : Game.Scope> {
    abstract val descriptor: GameDescriptor<S>

    abstract class Scope : ForwardingAudience {
        var joinable: Boolean = true

        val eventNode: EventNode<Event> = EventNode.all("")

        abstract val game: Game<*>

        abstract val players: Collection<Player>

        abstract fun addPlayer(player: Player)

        abstract fun removePlayer(player: Player)

        suspend inline fun <reified T : Event> until(crossinline block: (T) -> Boolean) =
            suspendCoroutine { continuation ->
                eventNode.addListener(
                    object : EventListener<T> {
                        private val eventType = T::class.java

                        override fun eventType(): Class<T> = eventType

                        override fun run(event: T): EventListener.Result {
                            if (block(event)) {
                                continuation.resume(Unit)
                                return EventListener.Result.EXPIRED
                            }

                            return EventListener.Result.SUCCESS
                        }
                    })
            }

        suspend inline fun <reified T : Event> waitFor(noinline filter: ((T) -> Boolean)? = null) =
            suspendCoroutine { continuation ->
                eventNode.addListener(
                    EventListener.builder(T::class.java)
                        .expireCount(1)
                        .let { if (filter != null) it.filter(filter) else it }
                        .handler { continuation.resume(Unit) }
                        .build())
            }
    }

    abstract suspend fun start(scope: S)

    abstract class InstanceScope(val instance: Instance) : Scope() {
        init {
            instance.eventNode().addListener(PlayerSpawnEvent::class.java) {
                addPlayer(it.player)
                eventNode.call(PlayerJoinGameEvent(this, it.player))
            }
            instance.eventNode().addListener(RemoveEntityFromInstanceEvent::class.java) {
                val player = it.entity as? Player ?: return@addListener
                eventNode.call(PlayerLeaveGameEvent(this, player))
            }
        }

        override val players: Collection<Player>
            get() = instance.players

        override fun audiences(): Iterable<Audience> = instance.audiences()

        override fun addPlayer(player: Player) {
            if (player.instance != instance) player.setInstance(instance)
        }

        override fun removePlayer(player: Player) {
            TODO("Removing players to a fallback")
        }
    }
}
