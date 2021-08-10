package cc.makin.pvpinstructor

import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.Locale
import java.util.logging.Level
import java.util.logging.Logger

class InstructorToggleUI(
    private val settingStorage: PvpInstructorSettingStorage,
) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) =
        if (args.size == 1) {
            toggleCommand(sender, args)
        } else {
            false
        }

    private fun toggleCommand(sender: CommandSender, args: Array<out String>) = if (sender is Player) {
        val enabled = when (args[0].toLowerCase(Locale.ROOT)) {
            "on", "włącz", "wlacz" -> true
            "off", "wyłącz", "wylacz" -> false
            else -> null
        }

        if (enabled != null) {
            setInstructorEnabled(sender, enabled)
            true
        } else {
            false
        }
    } else {
        sender.sendMessage("${ChatColor.RED}Musisz być graczem by móc wykonać te polecenie!")
        true
    }

    private fun setInstructorEnabled(sender: Player, enabled: Boolean) {
        sender.sendMessage("${ChatColor.YELLOW}Zapisywanie ustawienia...")
        settingStorage.setEnabledInstructor(sender.uniqueId, enabled)
            .whenComplete { _, th ->
                if (th != null) {
                    logger.log(Level.SEVERE, "Failed to save instructor setting for '${sender.name}'!", th)
                    sender.sendMessage("${ChatColor.RED}Wystąpił nieoczekiwany błąd podczas zapisu ustawienia!")
                } else {
                    sender.sendMessage("${ChatColor.GREEN}Pomyślnie zapisano ustawienie.")
                }
            }
    }

    companion object {
        // spigot api is used - so we cant include slf4j like i would do with paper as dependency
        private val logger = Logger.getLogger(InstructorToggleUI::class.java.name)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): List<String> = if (args.size == 1) {
        val input = args[0].toLowerCase(Locale.ROOT)
        listOf("on", "włącz", "wlacz", "off", "wyłącz", "wylacz")
            .filter { it.startsWith(input) }
    } else {
        emptyList()
    }
}