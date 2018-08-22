package me.cristiangomez.launcher

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import me.cristiangomez.launcher.view.shortcuts.ShortcutsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as LauncherApplication).database.appShortcutDao().getAll()
                .observe(this, Observer {
                    Log.d(MainActivity::class.java.canonicalName, it?.toString())
                })
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ShortcutsFragment.newInstance(),
                        FRAGMENT_TAG_SHORTCUTS)
                .commit()
//        val mainIntent = Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        val pkgAppsList = this.getPackageManager().queryIntentActivities( mainIntent, 0)
//        pkgAppsList.forEach {
//            Log.d(MainActivity::class.java.canonicalName, it.toString());
////        }
//        val intent = this.packageManager.getLaunchIntentForPackage("")
//        startActivity(intent)
    }

    companion object {
        val FRAGMENT_TAG_SHORTCUTS = "SHORTCUTS"
    }
}
