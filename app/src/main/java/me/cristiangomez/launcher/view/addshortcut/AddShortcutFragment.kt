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
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
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
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.core.content.ContextCompat.getSystemService
import me.cristiangomez.launcher.data.TutorialNewShortcutStep
import me.cristiangomez.launcher.util.SharedPreferencesManager


class AddShortcutFragment : Fragment() {

    companion object {
        fun newInstance() = AddShortcutFragment()
        const val FRAGMENT_TAG_AVAILABLE_APPS_DIALOG = "AVAILABLE_APPS_DIALOG"
    }

    private lateinit var viewModel: AddShortcutViewModel
    private var selectedApp: AvailableApp? = null
    private var availableAppsDialog: DialogFragment? = null
    private lateinit var preferencesManager: SharedPreferencesManager

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
                if (s!!.toString().toLowerCase() == "test") {
                    if (!preferencesManager.tutorialFinished && preferencesManager.tutorialAddNewShortcutCurrentStep == TutorialNewShortcutStep.SELECT_APP) {
                        TapTargetView.showFor(requireActivity(), TapTarget.forView(actionSelectApp, "Select app", "Click here to select from a list of your installed apps"), object : TapTargetView.Listener() {
                            override fun onTargetClick(view: TapTargetView?) {
                                super.onTargetClick(view)
                                actionSelectApp.callOnClick()
                                preferencesManager.tutorialAddNewShortcutCurrentStep = TutorialNewShortcutStep.CLICK_DONE_BUTTON
                            }

                            override fun onTargetCancel(view: TapTargetView?) {
                                super.onTargetCancel(view)
                                preferencesManager.tutorialFinished = false
                            }
                        })
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        actionAddApp.setOnClickListener { it ->
            if (selectedApp != null) {
                var label = selectedApp!!.label
                val userLabel = labelInputView.editText!!.text.toString()
                if (userLabel.isNotBlank()) {
                    label = userLabel
                }
                val newApp = AppShortcut(label = label,
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

        preferencesManager = (requireContext().applicationContext as LauncherApplication).sharedPreferencesManager

        if (!preferencesManager.tutorialFinished && preferencesManager.tutorialAddNewShortcutCurrentStep == TutorialNewShortcutStep.WRITE_LABEL) {
            TapTargetView.showFor(requireActivity(), TapTarget.forView(labelInputView, "Write some label", "If you don't we will use the app name, Go Ahead and write Test").id(1), object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView?) {
                    super.onTargetClick(view)
                    labelInputView.editText!!.requestFocus()
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.showSoftInput(labelInputView!!.editText, InputMethodManager.SHOW_IMPLICIT)
                    preferencesManager.tutorialAddNewShortcutCurrentStep = TutorialNewShortcutStep.SELECT_APP
                }

                override fun onTargetCancel(view: TapTargetView?) {
                    super.onTargetCancel(view)
                    preferencesManager.tutorialFinished = true
                }
            })
        }
        // TODO: Use the ViewModel
    }

    fun onAppSelected(app: AvailableApp) {
        selectedApp = app
        errorTextView.visibility = View.INVISIBLE
        showSelectedApp()
        if (!preferencesManager.tutorialFinished && preferencesManager.tutorialAddNewShortcutCurrentStep == TutorialNewShortcutStep.CLICK_DONE_BUTTON) {
            TapTargetView.showFor(requireActivity(), TapTarget.forView(actionAddApp, "Add shortcut", "Once done that it's all set, you can click here to add your new shortcut")
                    .transparentTarget(true), object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView?) {
                    super.onTargetClick(view)
                    actionAddApp.callOnClick()
                    preferencesManager.tutorialFinished = true
                }

                override fun onTargetCancel(view: TapTargetView?) {
                    super.onTargetCancel(view)
                    preferencesManager.tutorialFinished = false
                }
            })
        }
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
