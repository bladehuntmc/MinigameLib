package net.bladehunt.minigamelib

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.event.PlayerJoinGameEvent
import net.minestom.server.Viewable
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import net.minestom.server.instance.Instance

abstract class Game<S : Game.Scope> {
    abstract val descriptor: GameDescriptor<S>

    abstract class Scope : Viewable {
        val eventNode: EventNode<Event> = EventNode.all("")

        abstract val game: Game<*>

        var joinable: Boolean = true

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

    abstract class InstanceScope : Scope() {
        abstract val instance: Instance

        override fun addViewer(player: Player): Boolean =
            joinable &&
                !PlayerJoinGameEvent(this, player).apply(eventNode::call).isCancelled &&
                run {
                    if (player.instance == instance) false
                    else {
                        player.setInstance(instance)
                        true
                    }
                }

        override fun removeViewer(player: Player): Boolean {
            TODO("Not yet implemented")
        }

        override fun getViewers(): Set<Player> = instance.players
    }
}
