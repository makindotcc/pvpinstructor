package cc.makin.pvpinstructor

import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.awt.Color

class Instructor(
    private val settingStorage: PvpInstructorSettingStorage,
) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun handleCooldownReset(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if (damager is Player && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            handleCooldownReset(damager)
        }
    }

    private fun handleCooldownReset(player: Player) = run {
        if (this.settingStorage.hasEnabledInstructor(player.uniqueId)) {
            val cd = player.attackCooldown
            val message = getMessage(cd)
            val coloredMessage = successRateColor(cd).toString() + message
            player.sendTitle(" ", coloredMessage, 0, 5, 5)
        }
    }

    private fun getMessage(cooldown: Float) = when (cooldown) {
        in 0.0f..0.4f -> "fatalnie"
        in 0.4f..0.6f -> "słabo"
        in 0.6f..0.8f -> "średnio"
        in 0.8f..0.99f -> "prawie dobrze"
        else -> "idealnie"
    }

    private fun successRateColor(cooldownPercent: Float): ChatColor = run {
        val hue = cooldownPercent * 0.3f
        val saturation = 0.9f
        val brightness = 1.0f
        ChatColor.of(Color.getHSBColor(hue, saturation, brightness))
    }
}
