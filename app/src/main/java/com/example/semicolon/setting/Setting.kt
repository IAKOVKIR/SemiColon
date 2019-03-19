package com.example.semicolon.setting

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample pos for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object Setting {

    /**
     * An array of sample (dummy) items.
     */
    val SETTING: MutableList<SettingItem> = ArrayList()

    private val setting_names = arrayOf("Notifications", "Change Password", "Language", "Help", "About", "Log Out")

    /**
     * A map of sample (dummy) items, by ID.
     */
    val SETTINGS_MAP: MutableMap<String, SettingItem> = HashMap()

    private const val COUNT = 6

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addSetting(createSettingItem(i))
        }
    }

    private fun addSetting(setting: SettingItem) {
        SETTING.add(setting)
        SETTINGS_MAP[setting.name] = setting
    }

    private fun createSettingItem(position: Int): SettingItem {
        return SettingItem(setting_names[position - 1], "$position", "F")
    }

    /*private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        return builder.toString()
    }*/

    /**
     * A dummy item representing a piece of pos.
     */
    data class SettingItem(val name: String, val pos: String, val details: String) {
        override fun toString(): String = pos
    }
}
