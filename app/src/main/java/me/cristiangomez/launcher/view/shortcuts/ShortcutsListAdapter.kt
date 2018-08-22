package me.cristiangomez.launcher.view.shortcuts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.data.entity.AppShortcut
import me.cristiangomez.launcher.databinding.ShortcutItemRowBinding

class ShortcutsListAdapter(private val shortcuts: MutableList<AppShortcut> = mutableListOf(),
                           private val onShortcutSelected: ((shortcut: AppShortcut) -> Unit)? = null) :
        RecyclerView.Adapter<ShortcutsListAdapter.ShortcutViewHolder>() {
    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ShortcutItemRowBinding = DataBindingUtil.inflate<ShortcutItemRowBinding>(layoutInflater!!,
                R.layout.shortcut_item_row, parent, false)
        return ShortcutViewHolder(binding, onShortcutSelected)
    }

    override fun getItemCount(): Int {
        return shortcuts.size
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        holder.bind(shortcuts[position])
    }

    fun addItems(newShortcuts: List<AppShortcut>) {
        newShortcuts.forEach {
            val existentIndex = shortcuts.indexOf(it)
            if (existentIndex == -1) {
                shortcuts.add(it)
                notifyItemInserted(shortcuts.lastIndex)
            } else {
                shortcuts[existentIndex] = it
            }
        }
        val itemsToDelete = shortcuts.minus(newShortcuts)
        shortcuts.removeAll(itemsToDelete)
        itemsToDelete.forEachIndexed { index, _ ->
            notifyItemRemoved(index)
        }
    }

    class ShortcutViewHolder(private val binding: ShortcutItemRowBinding,
                             private val onShortcutSelected: ((shortcut: AppShortcut) -> Unit)? = null) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(shortcut: AppShortcut) {
            binding.shortcut = shortcut
            binding.root.setOnClickListener {
                onShortcutSelected?.invoke(shortcut)
            }
        }
    }
}