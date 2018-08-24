package me.cristiangomez.launcher.view.shortcuts

import android.content.Context
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
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.completable.CompletableFromCallable
import io.reactivex.schedulers.Schedulers
import me.cristiangomez.launcher.LauncherApplication
import me.cristiangomez.launcher.data.entity.AppShortcut
import com.getkeepsafe.taptargetview.TapTargetView
import android.graphics.Typeface
import com.getkeepsafe.taptargetview.TapTarget
import me.cristiangomez.launcher.data.TutorialNewShortcutStep


class ShortcutsFragment : Fragment() {

    companion object {
        fun newInstance() = ShortcutsFragment()
    }

    private lateinit var viewModel: ShortcutsViewModel
    private lateinit var listener: ShortcutsFragmentListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shortcuts_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as ShortcutsFragmentListener
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
        }, onShortcutRemove = {
            AlertDialog.Builder(requireContext())
                    .setPositiveButton(R.string.shortcuts_action_remove) { _, _ ->
                        removeShortcut(it)
                    }
                    .setNegativeButton(R.string.action_cancel) { _, _ -> }
                    .setTitle(R.string.dialog_remove_shortcut_title)
                    .setIcon(Drawable.createFromStream(requireContext().contentResolver.openInputStream(Uri.parse(it.iconPath)), null))
                    .setMessage(getString(R.string.dialog_remove_shortcut_message, it.label))
                    .setCancelable(true)
                    .create()
                    .show()
        }, onShortcutReorder = { dropped, target ->
            reorderShortcuts(dropped, target)
        })
        shortcutsListView.adapter = adapter
        viewModel.shortcutsLiveData.observe(this, Observer {
            adapter.addItems(it)
        })
        addShortcutAction.setOnClickListener {
            listener.onAddShortcut()
        }

        val preferencesManager = (requireContext().applicationContext as LauncherApplication).sharedPreferencesManager

        if (!preferencesManager.tutorialFinished && preferencesManager.tutorialAddNewShortcutCurrentStep == TutorialNewShortcutStep.CLICK_ADD_BUTTON) {
            TapTargetView.showFor(requireActivity(),
                    TapTarget.forView(addShortcutAction, "Add shortcuts", "You can add any app you have installed clicking here")
                            .transparentTarget(true),
                    object : TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        override fun onTargetClick(view: TapTargetView) {
                            super.onTargetClick(view)      // This call is optional
                            preferencesManager.tutorialAddNewShortcutCurrentStep = TutorialNewShortcutStep.WRITE_LABEL
                            listener.onAddShortcut()
                        }

                        override fun onTargetCancel(view: TapTargetView?) {
                            super.onTargetCancel(view)
                            preferencesManager.tutorialFinished = true
                        }
                    })
        }
    }

    private fun isPackageExisted(targetPackage: String): Boolean {
        val pm = requireContext().packageManager
        try {
            val info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

        return true
    }

    private fun removeShortcut(shortcut: AppShortcut) {
        CompletableFromCallable.create {
            (requireContext().applicationContext as LauncherApplication).database.appShortcutDao().deleteAll(shortcut)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun reorderShortcuts(dropped: AppShortcut, target: AppShortcut) {
        val droppedId = dropped.order!!
        dropped.order = target.order
        target.order = droppedId
        CompletableFromCallable.create {
            val dao = (requireContext().applicationContext as LauncherApplication).database.appShortcutDao()
            dao.updateAll(dropped, target)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

    }

    interface ShortcutsFragmentListener {
        fun onAddShortcut()
    }

}
