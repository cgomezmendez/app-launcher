package me.cristiangomez.launcher.view.shortcuts

import android.os.Build
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.annotation.LayoutRes
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.entity.AppShortcut
import me.cristiangomez.launcher.databinding.ShortcutItemRowBinding
import me.cristiangomez.launcher.view.AbstractRecyclerAdapter

class ShortcutsListAdapter(items: List<AppShortcut> = mutableListOf(),
                           onItemSelected: ((shortcut: AppShortcut) -> Unit)? = null,
                           private val onShortcutRemove: ((shortcut: AppShortcut) -> Unit)? = null,
                           private val onShortcutReorder: ((dropped: AppShortcut,
                                                            target: AppShortcut) -> Unit)? = null) :
        AbstractRecyclerAdapter<AppShortcut, ShortcutItemRowBinding>(items, onItemSelected) {
    @LayoutRes
    override fun getLayoutId(): Int {
        return R.layout.shortcut_item_row
    }

    override fun getViewHolder(binding: ShortcutItemRowBinding): BaseViewHolder<AppShortcut, ShortcutItemRowBinding> {
        return ShortcutViewHolder(binding, onShortcutSelected = onItemSelected,
                onShortcutRemove = onShortcutRemove, onShortcutReorder = onShortcutReorder)
    }

    override fun getItemId(position: Int): Long {
        return items[position].id!!
    }

    override fun getIdOfItem(item: AppShortcut): Int {
        return item.id!!.toInt()
    }


    class ShortcutViewHolder(binding: ShortcutItemRowBinding,
                             onShortcutSelected: ((shortcut: AppShortcut) -> Unit)? = null,
                             private val onShortcutRemove: ((shortcut: AppShortcut) -> Unit)? = null,
                             private val onShortcutReorder: ((dropped: AppShortcut, target: AppShortcut) -> Unit)? = null) :
            AbstractRecyclerAdapter.BaseViewHolder<AppShortcut, ShortcutItemRowBinding>(binding, onShortcutSelected) {
        override val containerView: View?
            get() = binding.root

        override fun bind(item: AppShortcut) {
            super.bind(item)
            binding.root.tag = item
            binding.actionRemoveShortcut.setOnClickListener {
                onShortcutRemove?.invoke(item)
            }
            binding.root.setOnDragListener { v, event ->
                when (event.action) {
                    DragEvent.ACTION_DROP -> {
                        val target = (v.tag as AppShortcut)
                        val dropped = (event.localState as AppShortcut)
                        onShortcutReorder?.invoke(dropped, target)
                        Log.d(ShortcutsListAdapter::class.java.canonicalName, target.id.toString() + " + " + dropped.id.toString())
                    }
                    else -> {
                    }
                }
                true
            }
            binding.root.setOnLongClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    it.startDragAndDrop(null, View.DragShadowBuilder(it), it.tag, 0)
                } else {
                    it.startDrag(null, View.DragShadowBuilder(it), it.tag, 0)
                }
            }
        }
    }
}