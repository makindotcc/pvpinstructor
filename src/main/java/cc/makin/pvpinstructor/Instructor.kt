package cc.makin.pvpinstructor

import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class Instructor(
    private val settingStorage: PvpInstructorSettingStorage,
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun handleAttack(event: PlayerInteractEvent) {
        when (event.action) {
            Action.LEFT_CLICK_AIR,
            Action.LEFT_CLICK_BLOCK -> handleAttack(event.player)
            else -> {}
        }
    }

    private fun handleAttack(player: Player) = run {
        if (this.settingStorage.hasEnabledInstructor(player.uniqueId)) {
            val message = getMessage(player.attackCooldown)
            player.sendTitle(" ", message, 0, 5, 5)
        }
    }

    private fun getMessage(cooldown: Float) = when (cooldown) {
        in 0.0f..0.4f -> "${ChatColor.DARK_RED}fatalnie"
        in 0.4f..0.6f -> "${ChatColor.RED}słabo"
        in 0.6f..0.8f -> "${ChatColor.YELLOW}średnio"
        in 0.8f..0.99f -> "${ChatColor.GREEN}prawie dobrze"
        else -> "${ChatColor.GREEN}idealnie"
    }
}
