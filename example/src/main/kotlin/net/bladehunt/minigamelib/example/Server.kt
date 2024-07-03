package net.bladehunt.minigamelib.example

import kotlinx.coroutines.*
import net.bladehunt.kotstom.GlobalEventHandler
import net.bladehunt.kotstom.InstanceManager
import net.bladehunt.kotstom.dsl.builder
import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSpawnEvent

suspend fun main() =
    CoroutineScope(Dispatchers.Default).run {
        val server = MinecraftServer.init()

        val instance = InstanceManager.createInstanceContainer()

        GlobalEventHandler.listen<AsyncPlayerConfigurationEvent> { event ->
            event.spawningInstance = instance
        }
        val scope = ExampleGame.Scope(instance)
        launch { ExampleGame.start(scope) }

        GlobalEventHandler.builder<PlayerSpawnEvent> {
            asyncHandler { event ->
                delay(5000)
                scope.addViewer(event.player)
            }
        }

        server.start("127.0.0.1", 25565)
    }
