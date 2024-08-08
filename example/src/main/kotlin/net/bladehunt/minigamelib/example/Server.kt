package net.bladehunt.minigamelib.example

import kotlinx.coroutines.coroutineScope
import net.bladehunt.kotstom.CommandManager
import net.bladehunt.kotstom.GlobalEventHandler
import net.bladehunt.kotstom.InstanceManager
import net.bladehunt.kotstom.coroutines.startSuspending
import net.bladehunt.kotstom.dsl.kommand.kommand
import net.bladehunt.kotstom.dsl.listen
import net.bladehunt.minigamelib.GameManager
import net.bladehunt.minigamelib.example.sneakoff.SneakOff
import net.bladehunt.minigamelib.ext.game
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.chunk.ChunkSupplier

lateinit var lobbyInstance: Instance

suspend fun main() = coroutineScope {
    val server = MinecraftServer.init()

    lobbyInstance =
        InstanceManager.createInstanceContainer().apply {
            chunkSupplier = ChunkSupplier(::LightingChunk)
            setGenerator { unit -> unit.modifier().fillHeight(0, 10, Block.BRICKS) }
        }

    GlobalEventHandler.listen<AsyncPlayerConfigurationEvent> { event ->
        event.spawningInstance = lobbyInstance
        event.player.respawnPoint = Pos(0.5, 11.0, 0.5)
    }

    CommandManager.register(
        kommand {
            name = "join"
            defaultExecutorAsync {
                val gameInstance =
                    InstanceManager.createInstanceContainer().apply {
                        chunkSupplier = ChunkSupplier(::LightingChunk)
                        setGenerator { unit -> unit.modifier().fillHeight(0, 10, Block.STONE) }
                        eventNode().listen<PlayerSpawnEvent> {
                            it.player.teleport(Pos(0.5, 11.0, 0.5))
                        }
                    }

                GameManager.getOrCreateFirstJoinableGame<SneakOff> { SneakOff(gameInstance) }
                    .addPlayer(player)
            }
        })

    CommandManager.register(
        kommand {
            name = "lobby"
            defaultExecutor { player.setInstance(lobbyInstance) }
        })

    CommandManager.register(
        kommand {
            name = "cancel"

            defaultExecutorAsync { player.game?.stop(true) }
        })

    server.startSuspending("127.0.0.1", 25565)
}
