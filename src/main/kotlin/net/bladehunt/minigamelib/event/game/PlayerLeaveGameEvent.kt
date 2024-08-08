package net.bladehunt.minigamelib.event.game

import net.bladehunt.minigamelib.Game
import net.minestom.server.entity.Player
import net.minestom.server.event.trait.CancellableEvent
import net.minestom.server.event.trait.PlayerEvent

data class PlayerLeaveGameEvent(override val game: Game, private val player: Player) :
    PlayerEvent, GameEvent, CancellableEvent {
    private var isCancelled = false

    override fun getPlayer(): Player = player

    override fun isCancelled(): Boolean = isCancelled

    override fun setCancelled(value: Boolean) {
        isCancelled = value
    }
}
