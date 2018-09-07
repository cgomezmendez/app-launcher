package me.cristiangomez.launcher.util

import me.cristiangomez.launcher.BuildConfig

object ImageUtil {
    fun getDrawablePath(drawableName: String): String {
        return "android.resource://${BuildConfig.APPLICATION_ID}/$drawableName"

    }
}