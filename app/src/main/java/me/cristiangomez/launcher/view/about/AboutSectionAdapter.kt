package me.cristiangomez.launcher.view.about

import androidx.annotation.LayoutRes
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.pojo.AboutSection
import me.cristiangomez.launcher.databinding.AboutItemRowBinding
import me.cristiangomez.launcher.view.AbstractRecyclerAdapter

class AboutSectionAdapter(items: List<AboutSection>,
                          onItemSelected: ((item: AboutSection) -> Unit)? = null) :
        AbstractRecyclerAdapter<AboutSection, AboutItemRowBinding>(items, onItemSelected) {

    @LayoutRes
    override fun getLayoutId(): Int {
        return R.layout.about_item_row
    }

    override fun getViewHolder(binding: AboutItemRowBinding): BaseViewHolder<AboutSection, AboutItemRowBinding> {
        return AboutViewHolder(binding)
    }

    class AboutViewHolder(binding: AboutItemRowBinding,
                          onItemSelected: ((item: AboutSection) -> Unit)? = null) :
            AbstractRecyclerAdapter.BaseViewHolder<AboutSection,
            AboutItemRowBinding>(binding, onItemSelected) {

    }
}