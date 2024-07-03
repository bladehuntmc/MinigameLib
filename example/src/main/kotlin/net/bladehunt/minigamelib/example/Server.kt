package net.bladehunt.minigamelib.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.bladehunt.kotstom.CommandManager
import net.bladehunt.kotstom.GlobalEventHandler
import net.bladehunt.kotstom.InstanceManager
import net.bladehunt.kotstom.dsl.kommand.kommand
import net.bladehunt.kotstom.dsl.listen
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.chunk.ChunkSupplier

suspend fun main() =
    CoroutineScope(Dispatchers.Default).run {
        val server = MinecraftServer.init()

        val spawnInstance = InstanceManager.createInstanceContainer()

        GlobalEventHandler.listen<AsyncPlayerConfigurationEvent> { event ->
            event.spawningInstance = spawnInstance
        }

        val gameInstance = InstanceManager.createInstanceContainer()
        gameInstance.chunkSupplier = ChunkSupplier(::LightingChunk)
        gameInstance.setGenerator { unit -> unit.modifier().fillHeight(0, 10, Block.STONE) }
        gameInstance.eventNode().listen<PlayerSpawnEvent> {
            it.player.teleport(Pos(0.5, 11.0, 0.5))
        }

        val instance = ExampleGame.GameInstance(gameInstance)
        launch { instance.start() }

        CommandManager.register(
            kommand {
                name = "join"
                defaultExecutor { instance.addPlayer(player) }
            })

        server.start("127.0.0.1", 25565)
    }
