package net.bladehunt.minigamelib

import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.instance.GameInstance

abstract class Game<T : GameInstance<T>> {
    abstract val descriptor: GameDescriptor<T>
}
