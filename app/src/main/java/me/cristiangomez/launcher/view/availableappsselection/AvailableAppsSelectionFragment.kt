package me.cristiangomez.launcher.view.availableappsselection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.available_apps_selection_fragment.*
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.pojo.AvailableApp

class AvailableAppsSelectionFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = AvailableAppsSelectionFragment()
    }

    private lateinit var viewModel: AvailableAppsSelectionViewModel
    private lateinit var listener: AvailableAppSelectionFragmentListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as AvailableAppSelectionFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.available_apps_selection_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressView.show()
        viewModel = ViewModelProviders.of(requireActivity()).get(AvailableAppsSelectionViewModel::class.java)
        appsListView.layoutManager = LinearLayoutManager(requireContext())

        appsListView.adapter = AvailableAppsAdapter(emptyList())


        viewModel.availableAppsLiveData.observe(requireActivity(), Observer { it ->
            if (progressView != null) {
                loadingTextView.visibility = View.INVISIBLE
                progressView.hide()
                appsListView.adapter = AvailableAppsAdapter(it) {
                    listener.onAppAvailableAppSelected(it)
                    dismiss()
                }
            }
        })
    }

    interface AvailableAppSelectionFragmentListener {
        fun onAppAvailableAppSelected(app: AvailableApp)
    }
}
