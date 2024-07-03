package net.bladehunt.minigamelib.example.sneakoff

import net.bladehunt.minigamelib.instance.InstancedGameInstance
import net.bladehunt.minigamelib.store.Store
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance

class SneakOffInstance(instance: Instance) :
    InstancedGameInstance<SneakOffInstance>(instance, SneakOff) {

    var Player.sneakCount by Store { 0 }
}
