package kr.dwkim.lezhinentexample.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.dwkim.lezhinentexample.R

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataList: ArrayList<T?> = ArrayList()

    private val loadMoreViewType = 999
    private var loadMoreIndex = 0

    val oneItemViewType = 99

    fun addAll(itemList: List<T>) {
        val prevSize = dataList.size
        dataList.addAll(itemList)
        notifyItemRangeInserted(prevSize, itemList.size)
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T? = dataList[position]

    fun showLoadMore(show: Boolean) {
        if(dataList.size > 0){
            if(show){
                if(loadMoreIndex == 0) {
                    dataList.add(null)
                    loadMoreIndex = dataList.indexOf(null)
                    notifyItemChanged(loadMoreIndex)
                }
            }else{
                if(dataList.size > loadMoreIndex && loadMoreIndex > 0) {
                    dataList.removeAt(loadMoreIndex)
                    notifyItemChanged(loadMoreIndex)
                    loadMoreIndex = 0
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return loadMoreViewType

        return internalGetItemViewType(item)
    }

    abstract fun internalGetItemViewType(item: T?) : Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context

        return when(viewType){
            loadMoreViewType -> LoadMoreHolder(
                LayoutInflater.from(context).inflate(R.layout.item_loadmore, parent, false)
            )
            else -> internalOnCreateViewHolder(context, parent, viewType)
        }
    }

    abstract fun internalOnCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder

    override fun getItemCount(): Int = dataList.size

    inner class LoadMoreHolder(itemView: View) : AndroidExtensionsViewHolder(itemView)
}