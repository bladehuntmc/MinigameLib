package net.bladehunt.minigamelib.event

import net.bladehunt.minigamelib.instance.GameInstance
import net.minestom.server.entity.Player
import net.minestom.server.event.trait.CancellableEvent
import net.minestom.server.event.trait.PlayerEvent

data class PlayerLeaveGameEvent<T : GameInstance<T>>(
    override val gameInstance: T,
    private val player: Player
) : PlayerEvent, GameInstanceEvent<T>, CancellableEvent {
    private var isCancelled = false

    override fun getPlayer(): Player = player

    override fun isCancelled(): Boolean = isCancelled

    override fun setCancelled(value: Boolean) {
        isCancelled = value
    }
}
