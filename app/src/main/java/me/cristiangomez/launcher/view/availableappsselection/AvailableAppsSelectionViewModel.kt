package me.cristiangomez.launcher.view.availableappsselection

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class AvailableAppsSelectionViewModel(application: Application) : AndroidViewModel(application) {
    var availableAppsLiveData: AvailableAppsLiveData = AvailableAppsLiveData(application)
}
