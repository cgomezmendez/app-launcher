package me.cristiangomez.launcher.view.availableappsselection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import me.cristiangomez.launcher.LauncherApplication
import me.cristiangomez.launcher.data.pojo.AvailableApp

class AvailableAppsSelectionViewModel(application: Application) : AndroidViewModel(application) {
    var availableAppsLiveData = MediatorLiveData<List<AvailableApp>>()
    var installedAppsSource = getApplication<LauncherApplication>().database.appShortcutDao().getAll();
    var availableAppsSource = AvailableAppsLiveData(application)
    var availableApps = listOf<AvailableApp>()

    init {
        availableAppsLiveData.addSource(availableAppsSource) { it ->
            availableApps = it
            availableAppsLiveData.postValue(it)
            availableAppsLiveData.removeSource(installedAppsSource)
            availableAppsLiveData.addSource(installedAppsSource) {
                val apps = availableApps.toMutableList()
                if (apps.isNotEmpty()) {
                    for (availableApp in availableApps) {
                        if (it.find
                                { appShortcut -> appShortcut.packageName == availableApp.packageName }
                                != null) {
                            apps.remove(availableApp)
                        }
                    }
                    availableAppsLiveData.postValue(apps)
                }
            }
        }
    }
}
