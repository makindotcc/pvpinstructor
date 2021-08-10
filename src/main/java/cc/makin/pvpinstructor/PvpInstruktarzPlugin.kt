package cc.makin.pvpinstructor

import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit

class PvpInstruktarzPlugin : JavaPlugin(), Listener {
    private lateinit var settingStorage: PvpInstructorSettingStorage
    private lateinit var instructor: Instructor
    private lateinit var toggleUI: InstructorToggleUI

    override fun onEnable() {
        settingStorage = MemoryPvpInstructorSettingStorage()
            .also { server.pluginManager.registerEvents(it, this) }
        postStorageInstall()
    }

    fun setStorage(newStorage: PvpInstructorSettingStorage) {
        // plugins should replace storage in server startup stage
        // so idc about async here
        settingStorage.destroy()
            .orTimeout(10, TimeUnit.SECONDS)
            .join()

        settingStorage = newStorage

        HandlerList.unregisterAll(instructor)
        postStorageInstall()
    }

    private fun postStorageInstall() {
        instructor = createInstructor()
        toggleUI = createToggleUI()
    }

    private fun createInstructor() = Instructor(settingStorage)
        .also { server.pluginManager.registerEvents(it, this) }

    private fun createToggleUI() = InstructorToggleUI(settingStorage)
        .also {
            val command = checkNotNull(getCommand("pvpinstruktarz")) {
                "pvpinstruktarz command is not registered!"
            }
            command.setExecutor(it)
            command.tabCompleter = it
        }
}
