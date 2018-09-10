package me.cristiangomez.launcher

import android.app.Application
import androidx.room.Room
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.cristiangomez.launcher.data.AppDatabase
import me.cristiangomez.launcher.data.entity.AppShortcut
import me.cristiangomez.launcher.network.ContactService
import me.cristiangomez.launcher.util.ImageUtil
import me.cristiangomez.launcher.util.SharedPreferencesManager

class LauncherApplication : Application() {
    lateinit var database: AppDatabase
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,
                AppDatabase::class.java, "launcher_application_db").build()
        sharedPreferencesManager = SharedPreferencesManager(this)
        if (!sharedPreferencesManager.didPopulateDatabase) {
            sharedPreferencesManager.didPopulateDatabase = true
            Single.fromCallable {
                val shortcuts = arrayOf(
                        AppShortcut(label = "Napco iBridge",
                                iconPath = ImageUtil.getDrawablePath("i_bridge"),
                                packageName = "com.napcosecurity.android.rcm.ui"),
                        AppShortcut(label = "nest",
                                iconPath = ImageUtil.getDrawablePath("nest"),
                                packageName = "com.nest.android"),
                        AppShortcut(label = "idms4500",
                                iconPath = ImageUtil.getDrawablePath("ivms"),
                                packageName = "com.mcu.iVMS"),
                        AppShortcut(label = "alarm.com",
                                iconPath = ImageUtil.getDrawablePath("alarm_mobile"),
                                packageName = "com.alarm.alarmmobile.android"),
                        AppShortcut(label = "lutron",
                                iconPath = ImageUtil.getDrawablePath("lutron"),
                                packageName = "com.lutron.lutronhomeplus")
                )
                database.appShortcutDao().insertAll(*shortcuts)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
        }
    }
}