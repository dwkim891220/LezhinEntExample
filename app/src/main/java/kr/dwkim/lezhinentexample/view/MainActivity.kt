package kr.dwkim.lezhinentexample.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_main.*
import kr.dwkim.lezhinentexample.R
import kr.dwkim.lezhinentexample.util.rx.ErrorState
import kr.dwkim.lezhinentexample.viewmodel.KakaoViewModel
import kr.dwkim.lezhinentexample.util.*
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    private val viewModel: KakaoViewModel by viewModel()
    private lateinit var listAdapter: SearchListAdapter

    private var lastSearchTimeMillis = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)

        initObserver()
        setLayouts()
        viewModel.clearVariables()
    }

    private fun initObserver(){
        viewModel.states.observe(this, Observer { state ->
            when(state){
                is KakaoViewModel.AddSearchResultState -> {
                    tv_aMain_empty.show(false)
                    listAdapter.addAll(state.list)
                }
                is KakaoViewModel.SearchResultIsEmptyState -> {
                    tv_aMain_empty.show()
                    listAdapter.clear()
                }
            }
        })

        viewModel.errorState.observe(this, Observer { state ->
            when(state){
                is ErrorState -> {
                    showErrorMessage(state.error)
                }
            }
        })
    }

    private fun setLayouts(){
        et_aMain.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                charSequence?.run {
                    lastSearchTimeMillis = System.currentTimeMillis()

                    Handler().postDelayed({
                        if(lastSearchTimeMillis + 1000L <= System.currentTimeMillis()) {
                            fetchSearchResult(this.toString())
                        }
                    }, 1000.toLong())
                }
            }
        })

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val lm = LinearLayoutManager(this@MainActivity)

        rv_aMain.run {
            adapter = SearchListAdapter(metrics.widthPixels).apply {
                listAdapter = this
                onClickItem = { url, widthRatio, heightRatio ->
                    startActivity(
                        Intent(this@MainActivity, DocumentDetailView::class.java).apply {
                            putExtra(DocumentDetailView.PARAMS_URL, url)
                            putExtra(DocumentDetailView.PARAMS_WIDTH_RATIO, widthRatio)
                            putExtra(DocumentDetailView.PARAMS_HEIGHT_RATIO, heightRatio)
                        }
                    )
                }
            }
            layoutManager = lm
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = lm.itemCount
                    val lastVisibleItem = lm.findLastVisibleItemPosition()

                    if (!viewModel.isLoading && totalItemCount <= lastVisibleItem + 5 && !viewModel.isEndPage) {
                        viewModel.searchImage(et_aMain.text.toString())
                    }
                }
            })
        }


    }

    private fun fetchSearchResult(keyword: String){
        listAdapter.clear()
        viewModel.clearVariables()
        viewModel.searchImage(keyword)
    }

    private fun showErrorMessage(throwable: Throwable){
        (throwable as? HttpException)?.response()?.errorBody()?.run {
            var msg = ""

            try {
                JsonParser().parse(this.string())?.also { jsonElement ->
                    when (jsonElement) {
                        is JsonObject -> {
                            jsonElement.asJsonObject.also { jObj ->
                                jObj.keySet()?.forEach { key ->
                                    if(jObj.has(key)){
                                        jObj.get(key).asJsonPrimitive.run {
                                            msg += String.format("%s : %s\n", key, this.asString)
                                        }
                                    }
                                }
                            }
                        }
                        else -> return@also
                    }
                }
            }catch (e: Exception){
                e.message?.run {
                    msg = this
                }
            }

            this@MainActivity.showDialog(msg)
        }
    }
}
