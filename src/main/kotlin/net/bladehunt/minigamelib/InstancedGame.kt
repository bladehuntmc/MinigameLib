package net.bladehunt.minigamelib

import net.bladehunt.minigamelib.event.game.PlayerJoinGameEvent
import net.bladehunt.minigamelib.event.game.PlayerLeaveGameEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import java.util.*

abstract class InstancedGame(uuid: UUID, val instance: Instance) : Game(uuid), ForwardingAudience {
    init {
        instance.eventNode().addListener(PlayerSpawnEvent::class.java) {
            MinecraftServer.getGlobalEventHandler().call(PlayerJoinGameEvent(this, it.player))
        }

        instance.eventNode().addListener(RemoveEntityFromInstanceEvent::class.java) {
            val player = it.entity as? Player ?: return@addListener

            MinecraftServer.getGlobalEventHandler().call(PlayerLeaveGameEvent(this, player))
        }
    }

    override val players: Collection<Player>
        get() = instance.players

    override fun addPlayer(player: Player) {
        player.setInstance(instance)
    }

    override fun removePlayer(player: Player) {
        player.sendToFallback()
    }

    override fun audiences(): MutableIterable<Audience> = instance.audiences()

    abstract fun Player.sendToFallback()
}
