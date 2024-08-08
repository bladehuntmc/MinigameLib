package net.bladehunt.minigamelib.ext

import net.bladehunt.minigamelib.GAME_TAG
import net.bladehunt.minigamelib.Game
import net.minestom.server.entity.Player

val Player.game: Game?
    get() = this.getTag(GAME_TAG)
