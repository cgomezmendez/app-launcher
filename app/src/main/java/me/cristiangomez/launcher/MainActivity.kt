package me.cristiangomez.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.cristiangomez.launcher.data.pojo.AvailableApp
import me.cristiangomez.launcher.view.addshortcut.AddShortcutFragment
import me.cristiangomez.launcher.view.availableappsselection.AvailableAppsSelectionFragment
import me.cristiangomez.launcher.view.shortcuts.ShortcutsFragment

class MainActivity : AppCompatActivity(), ShortcutsFragment.ShortcutsFragmentListener, AvailableAppsSelectionFragment.AvailableAppSelectionFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, ShortcutsFragment.newInstance(),
                            FRAGMENT_TAG_SHORTCUTS)
                    .commit()
        }
    }

    override fun onAddShortcut() {
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_ADD_SHORTCUT)
        if (fragment == null) {
            fragment = AddShortcutFragment.newInstance()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment, FRAGMENT_TAG_ADD_SHORTCUT).addToBackStack(null)
                .commit()
    }

    override fun onAppAvailableAppSelected(app: AvailableApp) {
        var fragment: AddShortcutFragment? = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_ADD_SHORTCUT) as AddShortcutFragment?
        if (fragment != null) {
            fragment.onAppSelected(app)
        }
    }

    companion object {
        const val FRAGMENT_TAG_SHORTCUTS = "SHORTCUTS"
        const val FRAGMENT_TAG_ADD_SHORTCUT = "ADD_SHORTCUT"
    }
}
