package me.cristiangomez.launcher.view.shortcuts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel;
import me.cristiangomez.launcher.LauncherApplication
import me.cristiangomez.launcher.data.entity.AppShortcut

class ShortcutsViewModel(application: Application) : AndroidViewModel(application) {
    val shortcutsLiveData: LiveData<List<AppShortcut>> = (application as LauncherApplication).database.appShortcutDao()
            .getAll()

}
