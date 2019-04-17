package kr.dwkim.lezhinentexample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
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
    private val listAdapter = SearchListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    listAdapter.clear()
                    viewModel.clearVariables()
                    viewModel.searchImage(this.toString())
                }
            }
        })

        rv_aMain.run {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun showErrorMessage(throwable: Throwable){
        (throwable as? HttpException)?.response()?.errorBody()?.run {
            var msg = ""

            try {
                JsonParser().parse(this.string())?.also { jsonElement ->
                    when (jsonElement) {
                        is JsonObject -> {
                            jsonElement.run {
                                this.keySet()?.forEach { key ->
                                    if(this.has(key)){
                                        this.getAsJsonArray(key).forEach { arrayElement ->
                                            if(msg.isNotEmpty()) msg += "\n"
                                            msg += arrayElement.asString
                                        }
                                    }
                                }
                            }
                        }
                        is JsonArray -> {
                            jsonElement.forEach { arrayElement ->
                                if(msg.isNotEmpty()) msg += "\n"
                                msg += arrayElement.asString
                            }
                        }
                        else -> {}
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
