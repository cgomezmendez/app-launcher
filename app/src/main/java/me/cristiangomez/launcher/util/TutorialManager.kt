package me.cristiangomez.launcher.util

class TutorialManager(val preferencesManager: SharedPreferencesManager) {
    fun getNextStep(currentScreen: String) {
        when (currentScreen) {
            SCREEN_SHORTCUTS -> {

            }
        }
    }

    companion object {
        const val SCREEN_SHORTCUTS = "SHORTCUTS"
        const val SCREEN_ADD_SHORTCUT = "ADD_SHORTCUT"
        const val SCREEN_SELECT_SHORTCUT = "SELECT_SHORTCUT"
    }
}