package com.example.semicolon.semi_settings

import java.util.ArrayList
import java.util.HashMap

object Setting {

    /**
     * An array of settings items.
     */
    val SETTING: MutableList<SettingItem> = ArrayList()

    private val setting_names: Array<String> = arrayOf("Notifications", "Change Password", "Language", "Help", "About", "Log Out")

    /**
     * A map of settings items, by ID.
     */
    private val SETTINGS_MAP: MutableMap<String, SettingItem> = HashMap()

    private const val COUNT = 6

    init {
        // Add settings items.
        for (i: Int in 1..COUNT)
            addSetting(createSettingItem(i))
    }

    private fun addSetting(setting: SettingItem) {
        SETTING.add(setting)
        SETTINGS_MAP[setting.name] = setting
    }

    private fun createSettingItem(position: Int): SettingItem {
        return SettingItem(
            setting_names[position - 1],
            position,
            "F"
        )
    }

    /**
     * A settings item representing a piece of pos.
     */
    data class SettingItem(val name: String, val pos: Int, val details: String) {
        override fun toString(): String = "$pos"
    }
}