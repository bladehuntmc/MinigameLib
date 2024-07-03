package net.bladehunt.minigamelib.instance

import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.event.PlayerJoinGameEvent
import net.bladehunt.minigamelib.event.PlayerLeaveGameEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.minestom.server.entity.Player
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance

open class InstancedGameInstance<T : InstancedGameInstance<T>>(
    val instance: Instance,
    game: Game<T>
) : AbstractGameInstance<T>(game), ForwardingAudience {

    init {
        instance.eventNode().apply {
            addListener(PlayerSpawnEvent::class.java) {
                addPlayer(it.player)
                gameEventNode.call(PlayerJoinGameEvent(this@InstancedGameInstance as T, it.player))
            }
            addListener(RemoveEntityFromInstanceEvent::class.java) {
                val player = it.entity as? Player ?: return@addListener
                gameEventNode.call(PlayerLeaveGameEvent(this@InstancedGameInstance as T, player))
            }
            addChild(gameEventNode as EventNode<out InstanceEvent>)
        }
    }

    override val players: Collection<Player>
        get() = instance.players

    override fun addPlayer(player: Player) {
        if (player.instance != instance) player.setInstance(instance)
    }

    override fun removePlayer(player: Player) {
        TODO("Removing players to a fallback")
    }

    override fun audiences(): Iterable<Audience> = instance.audiences()
}
