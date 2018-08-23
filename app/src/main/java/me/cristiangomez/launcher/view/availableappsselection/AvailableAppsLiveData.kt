package me.cristiangomez.launcher.view.availableappsselection

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.pojo.AvailableApp

class AvailableAppsLiveData(val context: Context) : LiveData<List<AvailableApp>>() {
    private val packageManager = context.packageManager!!

    init {
        loadAvailableApps()
    }

    private fun loadAvailableApps() {
        AsyncTask.execute {
            val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val availableApps: MutableList<AvailableApp> = mutableListOf()
            packages.forEach {
                val launchIntent = packageManager.getLaunchIntentForPackage(it.packageName)
                if (launchIntent != null && it.flags != ApplicationInfo.FLAG_SYSTEM && it.enabled && it.icon != 0) {
                    availableApps.add(AvailableApp(packageName = it.packageName,
                            icon = "android.resource://" + it.packageName + "/" + it.icon,
                            label = it.loadLabel(packageManager).toString()))
                }
            }
            postValue(availableApps)
        }
    }
}