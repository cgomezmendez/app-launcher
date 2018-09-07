package me.cristiangomez.launcher.view.availableappsselection

import androidx.annotation.LayoutRes
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.pojo.AvailableApp
import me.cristiangomez.launcher.databinding.AvailableAppItemRowBinding
import me.cristiangomez.launcher.view.AbstractRecyclerAdapter

class AvailableAppsAdapter(items: List<AvailableApp>,
                           onItemSelected: (((item: AvailableApp) -> Unit)?) = null) :
        AbstractRecyclerAdapter<AvailableApp, AvailableAppItemRowBinding>(items,
                onItemSelected) {

    @LayoutRes
    override fun getLayoutId(): Int {
        return R.layout.available_app_item_row
    }

    override fun getViewHolder(binding: AvailableAppItemRowBinding): BaseViewHolder<AvailableApp, AvailableAppItemRowBinding> {
        return AvailableAppsViewHolder(binding, onItemSelected)
    }

    class AvailableAppsViewHolder(binding: AvailableAppItemRowBinding,
                                  onItemSelected: (((item: AvailableApp) -> Unit)?) = null) :
            BaseViewHolder<AvailableApp, AvailableAppItemRowBinding>(binding,
                    onItemSelected) {
    }
}