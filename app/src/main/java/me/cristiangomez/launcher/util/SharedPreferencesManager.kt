package me.cristiangomez.launcher.util

import android.content.Context
import me.cristiangomez.launcher.data.TutorialNewShortcutStep

class SharedPreferencesManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE)

    var didPopulateDatabase: Boolean
        get() {
            return sharedPreferences.getBoolean(PREF_DID_POPULATE_DATABASE, false)
        }
        set(value) {
            sharedPreferences.edit().putBoolean(PREF_DID_POPULATE_DATABASE, value).apply()
        }

    var tutorialAddNewShortcutCurrentStep: TutorialNewShortcutStep?
    get() {
        return TutorialNewShortcutStep.valueOf(
                sharedPreferences.getString(PREF_TUTORIAL_ADD_NEW_SHORTCUT_CURRENT_STEP,
                        TutorialNewShortcutStep.CLICK_ADD_BUTTON.name)!!
        )
    }
    set(value) {
        sharedPreferences.edit()
                .putString(PREF_TUTORIAL_ADD_NEW_SHORTCUT_CURRENT_STEP, value?.name)
                .apply()
    }

    var tutorialFinished: Boolean
    get() {
        return sharedPreferences.getBoolean(PREF_TUTORIAL_FINISHED, false)
    }
    set(value) {
        sharedPreferences.edit()
                .putBoolean(PREF_TUTORIAL_FINISHED, value)
                .apply()
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "me.cristiangomez.launcher"
        const val PREF_DID_POPULATE_DATABASE = "DID_POPULATE_DATABASE"
        const val PREF_TUTORIAL_FINISHED = "PREF_TUTORIAL_FINISHED"
        const val PREF_TUTORIAL_ADD_NEW_SHORTCUT_CURRENT_STEP = "PREF_TUTORIAL_ADD_NEW_SHORTCUT_CURRENT_STEP"
    }
}