package me.cristiangomez.launcher.view.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.about_fragment.*
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.pojo.AboutSection

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceBlankState: Bundle?): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutViewModel::class.java)
        // TODO: Use the ViewModel
        aboutListView.layoutManager = LinearLayoutManager(requireContext())
        aboutListView.adapter = AboutSectionAdapter(
                listOf(
                        AboutSection(getString(R.string.about_who_we_are_title),
                                getString(R.string.about_who_we_are_text)),
                        AboutSection(getString(R.string.about_our_mission_title),
                                getString(R.string.about_our_mission_text)),
                        AboutSection(getString(R.string.about_our_team_title),
                                getString(R.string.about_our_team_text))
                )
        )
        getSupportActionBar()?.setSubtitle(R.string.about_title)

    }

    override fun onPause() {
        super.onPause()
        getSupportActionBar()?.subtitle = null
    }

    private fun getSupportActionBar() : ActionBar? {
        return (requireActivity() as AppCompatActivity).supportActionBar
    }

}
