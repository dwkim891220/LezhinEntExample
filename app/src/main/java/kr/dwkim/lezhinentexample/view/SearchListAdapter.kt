package kr.dwkim.lezhinentexample.view

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.item_document.view.*
import kr.dwkim.lezhinentexample.R
import kr.dwkim.lezhinentexample.model.DocumentModel

class SearchListAdapter(private val screenWidth: Int) : BaseRecyclerViewAdapter<DocumentModel>() {
    lateinit var onClickItem: (detailUrl: String, widthRatio: Int, heightRatio: Int) -> Unit

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
            getItem(position)?.also { item ->
                itemView.tv_itemDocument.text = item.image_url ?: "url 없음"
                itemView.iv_itemDocument.run {
                    if (item.width != null && item.height != null) {
                        val widthRatio = item.width
                        val heightRatio = item.height

                        val horizontalPadding = context.resources.getDimension(R.dimen.defaultPadding)
                        val ivWidth = screenWidth - (horizontalPadding * 2)
                        val ivHeight = ivWidth * heightRatio / widthRatio

                        layoutParams = LinearLayout.LayoutParams(ivWidth.toInt(), ivHeight.toInt())

                        val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(item.image_url))
                            .setProgressiveRenderingEnabled(true)
                            .build()

                        controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(controller)
                            .build()
                    }
                }

                itemView.setOnClickListener {
                    if(::onClickItem.isInitialized) {
                        if(item.image_url != null && item.width != null && item.height != null) {
                            onClickItem(item.image_url, item.width, item.height)
                        }
                    }
                }
            }
        }
    }

    inner class DocumentViewHolder(itemView: View) : AndroidExtensionsViewHolder(itemView)
}