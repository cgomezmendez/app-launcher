package me.cristiangomez.launcher.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "app_shortcut")
data class AppShortcut(@PrimaryKey(autoGenerate = true) var id: Long? = null,
                       @ColumnInfo(name = "label") val label: String,
                       @ColumnInfo(name = "package_name") val packageName: String,
                       @ColumnInfo(name = "icon_path") val iconPath: String) {
    override fun equals(other: Any?): Boolean {
        if (other is AppShortcut) {
            return other === this || other.id === this.id
        }
        return false
    }
}