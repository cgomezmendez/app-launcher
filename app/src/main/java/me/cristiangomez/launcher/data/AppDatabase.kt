package me.cristiangomez.launcher.data

import androidx.room.Database
import androidx.room.RoomDatabase
import me.cristiangomez.launcher.data.dao.AppShortcutDao
import me.cristiangomez.launcher.data.entity.AppShortcut

@Database(entities = [(AppShortcut::class)], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appShortcutDao(): AppShortcutDao

}