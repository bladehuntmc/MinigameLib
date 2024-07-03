package net.bladehunt.minigamelib.element

import net.bladehunt.minigamelib.Game

fun interface GameElement<S : Game.Scope> {
    suspend fun S.run()
}