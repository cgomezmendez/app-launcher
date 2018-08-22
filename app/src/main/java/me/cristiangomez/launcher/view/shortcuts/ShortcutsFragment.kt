package me.cristiangomez.launcher.view.shortcuts

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.shortcuts_fragment.*

import me.cristiangomez.launcher.R
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.content.Intent
import android.net.Uri


class ShortcutsFragment : Fragment() {

    companion object {
        fun newInstance() = ShortcutsFragment()
    }

    private lateinit var viewModel: ShortcutsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shortcuts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShortcutsViewModel::class.java)
        shortcutsListView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL,
                false)
        val adapter = ShortcutsListAdapter(onShortcutSelected = {
            if (isPackageExisted(it.packageName)) {
                val intent = requireContext().packageManager.getLaunchIntentForPackage(it.packageName)
                startActivity(intent)
            } else {
                val appPackageName = requireContext().getPackageName() // getPackageName() from Context or Activity object
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${it.packageName}")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${it.packageName}")))
                }

            }
        })
        shortcutsListView.adapter = adapter
        viewModel.shortcutsLiveData.observe(this, Observer {
            adapter.addItems(it)
        })
        // TODO: Use the ViewModel
    }

    fun isPackageExisted(targetPackage: String): Boolean {
        val pm = requireContext().getPackageManager()
        try {
            val info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

        return true
    }

}
