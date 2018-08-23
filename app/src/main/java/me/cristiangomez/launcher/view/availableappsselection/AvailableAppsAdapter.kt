package me.cristiangomez.launcher.view.availableappsselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.entity.AppShortcut
import me.cristiangomez.launcher.data.pojo.AvailableApp
import me.cristiangomez.launcher.databinding.AvailableAppItemRowBinding

class AvailableAppsAdapter(private val availableApps: List<AvailableApp>,
                           private val onAppSelected: ((app: AvailableApp) -> Unit)? = null) : RecyclerView.Adapter<AvailableAppsAdapter.AvailableAppsViewHolder>() {
    var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableAppsViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<AvailableAppItemRowBinding>(layoutInflater!!,
                R.layout.available_app_item_row, parent, false)
        return AvailableAppsViewHolder(binding, onAppSelected)
    }

    override fun getItemCount(): Int {
        return availableApps.size
    }

    override fun onBindViewHolder(holder: AvailableAppsViewHolder, position: Int) {
        holder.bind(availableApps[position])
    }

    class AvailableAppsViewHolder(private val binding: AvailableAppItemRowBinding,
                                  private val onAppSelected: ((app: AvailableApp) -> Unit)? = null) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AvailableApp) {
            binding.app = app
            binding.root.setOnClickListener {
                onAppSelected?.invoke(app)
            }
        }
    }
}