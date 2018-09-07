package me.cristiangomez.launcher.data.pojo


data class AvailableApp(val packageName: String,
                        val icon: String,
                        val label: String) : Comparable<AvailableApp> {
    override fun compareTo(other: AvailableApp): Int {
        if (other.packageName == packageName &&
                icon == other.icon &&
                label == other.label) {
            return 0
        }
        return -1
    }
}