package net.bladehunt.minigamelib

import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.instance.GameInstance

interface Game<T : GameInstance<T>> {
    val descriptor: GameDescriptor<T>
}
