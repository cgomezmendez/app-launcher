package me.cristiangomez.launcher.view.shortcuts

import android.os.Build
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.entity.AppShortcut
import me.cristiangomez.launcher.databinding.ShortcutItemRowBinding

class ShortcutsListAdapter(private var shortcuts: List<AppShortcut> = mutableListOf(),
                           private val onShortcutSelected: ((shortcut: AppShortcut) -> Unit)? = null,
                           private val onShortcutRemove: ((shortcut: AppShortcut) -> Unit)? = null,
                           private val onShortcutReorder: ((dropped: AppShortcut, target: AppShortcut) -> Unit)? = null) :
        RecyclerView.Adapter<ShortcutsListAdapter.ShortcutViewHolder>() {
    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ShortcutItemRowBinding = DataBindingUtil.inflate(layoutInflater!!,
                R.layout.shortcut_item_row, parent, false)
        return ShortcutViewHolder(binding, onShortcutSelected = onShortcutSelected, onShortcutRemove = onShortcutRemove, onShortcutReorder = onShortcutReorder)
    }

    override fun getItemCount(): Int {
        return shortcuts.size
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        holder.bind(shortcuts[position])
    }

    override fun getItemId(position: Int): Long {
        return shortcuts[position].id!!
    }

    fun addItems(newShortcuts: List<AppShortcut>) {
        val diffResult = DiffUtil.calculateDiff(ItemsDiffCallback(shortcuts, newShortcuts))
        shortcuts = newShortcuts
        diffResult.dispatchUpdatesTo(this)
    }

    class ShortcutViewHolder(private val binding: ShortcutItemRowBinding,
                             private val onShortcutSelected: ((shortcut: AppShortcut) -> Unit)? = null,
                             private val onShortcutRemove: ((shortcut: AppShortcut) -> Unit)? = null,
                             private val onShortcutReorder: ((dropped: AppShortcut, target: AppShortcut) -> Unit)? = null) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(shortcut: AppShortcut) {
            binding.shortcut = shortcut
            binding.root.tag = shortcut
            binding.root.setOnClickListener {
                onShortcutSelected?.invoke(shortcut)
            }
            binding.actionRemoveShortcut.setOnClickListener {
                onShortcutRemove?.invoke(shortcut)
            }
            binding.root.setOnDragListener { v, event ->
                when (event.action) {
                    DragEvent.ACTION_DROP -> {
                        var target = (v.tag as AppShortcut)
                        var dropped = (event.localState as AppShortcut)
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

    inner class ItemsDiffCallback(val oldList: List<AppShortcut>, val newList: List<AppShortcut>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].areContentsEquals(newList[newItemPosition])
        }
    }
}