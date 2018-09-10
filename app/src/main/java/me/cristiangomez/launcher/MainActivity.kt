package me.cristiangomez.launcher

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import me.cristiangomez.launcher.data.pojo.AvailableApp
import me.cristiangomez.launcher.view.about.AboutFragment
import me.cristiangomez.launcher.view.addshortcut.AddShortcutFragment
import me.cristiangomez.launcher.view.availableappsselection.AvailableAppsSelectionFragment
import me.cristiangomez.launcher.view.contact.ContactFragment
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
        setSupportActionBar(appBar)
        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar!!.setDisplayHomeAsUpEnabled(shouldDisplayHomeUp())
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar!!.setDisplayHomeAsUpEnabled(shouldDisplayHomeUp())
    }


    private fun shouldDisplayHomeUp(): Boolean {
        return supportFragmentManager.backStackEntryCount > 0
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_about -> {
                switchFragment(FRAGMENT_TAG_ABOUT)
            }
            R.id.action_contact -> {
                switchFragment(FRAGMENT_TAG_CONTACT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchFragment(fragmentTag: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, getFragment(fragmentTag)!!, fragmentTag)
                .addToBackStack(null)
                .commit()
    }

    private fun getFragment(fragmentTag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(fragmentTag) ?: return when (fragmentTag) {
            FRAGMENT_TAG_ABOUT ->
                AboutFragment.newInstance()
            FRAGMENT_TAG_ADD_SHORTCUT ->
                AddShortcutFragment.newInstance()
            FRAGMENT_TAG_SHORTCUTS ->
                ShortcutsFragment.newInstance()
            FRAGMENT_TAG_CONTACT ->
                ContactFragment.newInstance()
            else -> {
                null
            }
        }
    }

    override fun onAddShortcut() {
        switchFragment(FRAGMENT_TAG_ADD_SHORTCUT)
    }

    override fun onAppAvailableAppSelected(app: AvailableApp) {
        (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_ADD_SHORTCUT) as AddShortcutFragment?)?.onAppSelected(app)
    }

    companion object {
        const val FRAGMENT_TAG_SHORTCUTS = "SHORTCUTS"
        const val FRAGMENT_TAG_ADD_SHORTCUT = "ADD_SHORTCUT"
        const val FRAGMENT_TAG_ABOUT = "ABOUT"
        const val FRAGMENT_TAG_CONTACT = "CONTACT"
    }
}
