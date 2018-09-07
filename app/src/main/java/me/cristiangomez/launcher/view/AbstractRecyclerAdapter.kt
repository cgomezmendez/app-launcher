package me.cristiangomez.launcher.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import me.cristiangomez.launcher.BR

abstract class AbstractRecyclerAdapter<T : Comparable<T>, E : ViewDataBinding>(var items: List<T> = emptyList(),
                                                                               val onItemSelected: (((item: T) -> Unit)?)) :
        RecyclerView.Adapter<AbstractRecyclerAdapter.BaseViewHolder<T, E>>() {
    var layoutInflater: LayoutInflater? = null

    abstract fun getLayoutId(): Int
    abstract fun getViewHolder(binding: E): BaseViewHolder<T, E>

    open fun getIdOfItem(item: T): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, E> {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<E>(layoutInflater!!, getLayoutId(), parent, false)
        return getViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T, E>, position: Int) {
        holder.bind(items[position])
    }

    fun addItems(newItems: List<T>) {
        val diffResult = DiffUtil.calculateDiff(ItemsDiffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    open class BaseViewHolder<T, E : ViewDataBinding>(protected open val binding: E,
                                                      protected val onItemSelected: (((item: T) -> Unit)?) = null) : RecyclerView.ViewHolder(binding.root),
            LayoutContainer {
        open fun bind(item: T) {
            binding.setVariable(BR.item, item)
            if (onItemSelected != null) {
                binding.root.setOnClickListener {
                    onItemSelected.invoke(item)
                }
            }
        }

        override val containerView: View?
            get() = binding.root
    }

    inner class ItemsDiffCallback(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return getIdOfItem(oldList[oldItemPosition]) == getIdOfItem(newList[newItemPosition])
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].compareTo(newList[newItemPosition]) == 0
        }
    }
}