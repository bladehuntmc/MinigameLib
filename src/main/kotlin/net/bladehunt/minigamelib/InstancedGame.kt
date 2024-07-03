package net.bladehunt.minigamelib

import net.bladehunt.minigamelib.instance.InstancedGameInstance
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance

interface InstancedGame<T : InstancedGameInstance<T>> : Game<T> {
    fun getFallback(instance: T, player: Player): Instance
}
