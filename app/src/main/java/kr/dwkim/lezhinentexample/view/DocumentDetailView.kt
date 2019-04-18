package kr.dwkim.lezhinentexample.view

import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.activity_document_detail.*
import kr.dwkim.lezhinentexample.R
import kr.dwkim.lezhinentexample.util.argument

class DocumentDetailView : AppCompatActivity() {
    companion object{
        const val PARAMS_URL = "paramsUrl"
        const val PARAMS_WIDTH_RATIO = "paramsWidth"
        const val PARAMS_HEIGHT_RATIO = "paramsHeight"
    }

    private val url by argument<String>(PARAMS_URL)
    private val widthRatio by argument<Int>(PARAMS_WIDTH_RATIO)
    private val heightRatio by argument<Int>(PARAMS_HEIGHT_RATIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_detail)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val ivWidth = metrics.widthPixels
        val ivHeight = ivWidth * heightRatio / widthRatio

        iv_aDocumentDetail.run {
            layoutParams = LinearLayout.LayoutParams(ivWidth, ivHeight)

            val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build()

            controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(controller)
                .build()
        }

        btn_aDocumentDetail.setOnClickListener {
            finish()
        }
    }
}