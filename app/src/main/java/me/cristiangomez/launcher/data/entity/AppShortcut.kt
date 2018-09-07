package me.cristiangomez.launcher.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "app_shortcut", indices = [Index(value = ["package_name"], unique = true)])
data class AppShortcut(@PrimaryKey(autoGenerate = true) var id: Long? = null,
                       @ColumnInfo(name = "label") val label: String,
                       @ColumnInfo(name = "package_name") val packageName: String,
                       @ColumnInfo(name = "icon_path") val iconPath: String,
                       @ColumnInfo(name = "order") var order: Int? = null) : Comparable<AppShortcut> {
    init {
        if (order == null) {
            order = id?.toInt()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is AppShortcut) {
            return other === this || other.id === this.id
        }
        return false
    }

    override fun compareTo(other: AppShortcut): Int {
        if (other.packageName == this.packageName && other.label == this.label &&
                other.iconPath == this.iconPath) {
            return 0
        }
        return 1
    }
}