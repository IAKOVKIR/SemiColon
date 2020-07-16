package com.example.semicolon.semi_settings.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel for SettingsFragment.
 */
class SettingsFragmentViewModel: ViewModel() {

    /**
     * Variable that tells the Fragment to navigate to a specific SettingsFragment
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _settingId = MutableLiveData<Int>()
    /**
     * If this is non-null, immediately navigate to SettingsFragment and call [onSettingNavigated]
     */
    val settingId
        get() = _settingId

    fun onUserClicked(id: Int) {
        _settingId.value = id
    }

    /**
     * Call this immediately after navigating to SettingsFragment.
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun onSettingNavigated() {
        _settingId.value = null
    }

}