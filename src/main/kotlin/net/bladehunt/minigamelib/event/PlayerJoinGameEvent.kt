package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.Game
import net.minestom.server.entity.Player
import net.minestom.server.event.trait.CancellableEvent
import net.minestom.server.event.trait.PlayerEvent

data class PlayerJoinGameEvent<T : Game.Scope>(
    override val scope: T,
    private val player: Player
) : PlayerEvent, GameScopeEvent<T>, CancellableEvent {
    private var isCancelled = false

    override fun getPlayer(): Player = player

    override fun isCancelled(): Boolean = isCancelled

    override fun setCancelled(value: Boolean) {
        isCancelled = value
    }
}