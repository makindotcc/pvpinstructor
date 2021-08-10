package cc.makin.pvpinstructor

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface PvpInstructorSettingStorage {
    fun hasEnabledInstructor(playerId: UUID): Boolean

    fun setEnabledInstructor(playerId: UUID, enabled: Boolean): CompletableFuture<Void>

    fun destroy(): CompletableFuture<Void>
}

internal class MemoryPvpInstructorSettingStorage : PvpInstructorSettingStorage, Listener {
    private val players: MutableSet<UUID> = HashSet()

    override fun hasEnabledInstructor(playerId: UUID) = players.contains(playerId)

    override fun setEnabledInstructor(playerId: UUID, enabled: Boolean): CompletableFuture<Void> = run {
        if (enabled) {
            players.add(playerId)
        } else {
            players.remove(playerId)
        }

        CompletableFuture.completedFuture(null)
    }

    override fun destroy(): CompletableFuture<Void> = run {
        HandlerList.unregisterAll(this)

        CompletableFuture.completedFuture(null)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun disposeState(event: PlayerQuitEvent) {
        players.remove(event.player.uniqueId)
    }
}
