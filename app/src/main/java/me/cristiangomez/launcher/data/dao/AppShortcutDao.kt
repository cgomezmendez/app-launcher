package me.cristiangomez.launcher.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import me.cristiangomez.launcher.data.entity.AppShortcut

@Dao
interface AppShortcutDao {
    @Query(value = "SELECT * from app_shortcut") fun getAll(): LiveData<List<AppShortcut>>
    @Insert fun insertAll(vararg appShortcuts: AppShortcut)
    @Delete fun deleteAll(vararg  appShortcuts: AppShortcut)
    @Update fun updateAll(vararg  appShortcuts: AppShortcut)
}