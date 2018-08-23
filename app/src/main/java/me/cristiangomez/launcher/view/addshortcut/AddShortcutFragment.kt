package me.cristiangomez.launcher.view.addshortcut

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleFromCallable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_shortcut_fragment.*
import kotlinx.android.synthetic.main.shortcuts_fragment.*
import me.cristiangomez.launcher.LauncherApplication

import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.entity.AppShortcut
import me.cristiangomez.launcher.data.pojo.AvailableApp
import me.cristiangomez.launcher.view.availableappsselection.AvailableAppsSelectionFragment

class AddShortcutFragment : Fragment() {

    companion object {
        fun newInstance() = AddShortcutFragment()
        const val FRAGMENT_TAG_AVAILABLE_APPS_DIALOG = "AVAILABLE_APPS_DIALOG"
    }

    private lateinit var viewModel: AddShortcutViewModel
    private var selectedApp: AvailableApp? = null
    private var availableAppsDialog: DialogFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_shortcut_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddShortcutViewModel::class.java)
        actionSelectApp.setOnClickListener {
            if (availableAppsDialog == null) {
                availableAppsDialog = AvailableAppsSelectionFragment.newInstance()
            }
            availableAppsDialog?.show(fragmentManager, FRAGMENT_TAG_AVAILABLE_APPS_DIALOG)
        }
        labelInputView.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (errorTextView.visibility == View.VISIBLE) {
                    errorTextView.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        actionAddApp.setOnClickListener {
            if (selectedApp != null) {
                var label = selectedApp!!.label
                var userLabel = labelInputView.editText!!.text.toString()
                if (userLabel.isNotBlank()) {
                    label = userLabel
                }
                var newApp = AppShortcut(label = label,
                        iconPath = selectedApp!!.icon,
                        packageName = selectedApp!!.packageName)
                SingleFromCallable<Boolean> {
                    (requireContext().applicationContext as LauncherApplication).database.appShortcutDao().insertAll(
                            newApp
                    )
                    true
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturn {
                            false
                        }
                        .doOnSuccess {
                            if (it) {
                                fragmentManager?.popBackStack()
                            } else {
                                errorTextView.visibility = View.VISIBLE
                            }
                        }
                        .subscribe()
            }
        }
        // TODO: Use the ViewModel
    }

    fun onAppSelected(app: AvailableApp) {
        selectedApp = app
        errorTextView.visibility = View.INVISIBLE
        showSelectedApp()
    }

    private fun showSelectedApp() {
        if (actionSelectApp != null) {
            actionSelectApp.text = selectedApp?.label
            actionSelectApp.icon = Drawable.createFromStream(requireContext().contentResolver.openInputStream(Uri.parse(selectedApp!!.icon)),
                    null)
            actionSelectApp.iconTint = null
        }
    }
}
