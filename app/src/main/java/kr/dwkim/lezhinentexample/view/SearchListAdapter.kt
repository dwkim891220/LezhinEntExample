package kr.dwkim.lezhinentexample.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_document.view.*
import kr.dwkim.lezhinentexample.R
import kr.dwkim.lezhinentexample.model.DocumentModel

class SearchListAdapter : BaseRecyclerViewAdapter<DocumentModel>() {
    override fun internalGetItemViewType(item: DocumentModel?): Int = oneItemViewType

    override fun internalOnCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        DocumentViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_document, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? DocumentViewHolder)?.run {
            itemView.tv_itemDocument.text = getItem(position)?.image_url ?: "url 없음"
        }
    }

    inner class DocumentViewHolder(itemView: View) : AndroidExtensionsViewHolder(itemView)
}