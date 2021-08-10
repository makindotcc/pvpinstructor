package cc.makin.pvpinstructor

import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class PvpInstruktarz : JavaPlugin(), Listener {
    override fun onEnable() = run {
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun handleAttack(event: PlayerInteractEvent) {
        when (event.action) {
            Action.LEFT_CLICK_AIR,
            Action.LEFT_CLICK_BLOCK -> handleAttack(event.player)
            else -> {}
        }
    }

    private fun handleAttack(player: Player) = run {
        val message = getMessage(player.attackCooldown)
        player.sendTitle(" ", message, 0, 5, 5)
    }

    private fun getMessage(cooldown: Float) = when (cooldown) {
        in 0.0f..0.2f -> "${ChatColor.DARK_RED}fatalnie"
        in 0.2f..0.4f -> "${ChatColor.RED}słabo"
        in 0.4f..0.8f -> "${ChatColor.YELLOW}średnio"
        in 0.8f..0.99f -> "${ChatColor.GREEN}prawie dobrze"
        else -> "${ChatColor.GREEN}idealnie"
    }
}
