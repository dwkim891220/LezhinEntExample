package kr.dwkim.lezhinentexample.viewmodel

import androidx.lifecycle.MutableLiveData
import kr.dwkim.lezhinentexample.model.DocumentModel
import kr.dwkim.lezhinentexample.repository.IRepository
import kr.dwkim.lezhinentexample.util.rx.ErrorState
import kr.dwkim.lezhinentexample.util.rx.RxViewModel
import kr.dwkim.lezhinentexample.util.rx.SchedulerProvider
import kr.dwkim.lezhinentexample.util.rx.ViewModelState
import kr.dwkim.lezhinentexample.util.with

class KakaoViewModel(
    private val repository: IRepository,
    private val schedulerProvider: SchedulerProvider
) : RxViewModel() {
    val states = MutableLiveData<ViewModelState>()
    val errorState = MutableLiveData<ViewModelState>()

    var page = 1
    var isEndPage = false

    fun clearVariables(){
        page = 1
        isEndPage = false
    }

    data class AddSearchResultState(val list: List<DocumentModel>) : ViewModelState()
    object SearchResultIsEmptyState : ViewModelState()
    fun searchImage(keyword: String){
        cleanLaunch {
            repository.searchImage(keyword, page)
                .with(schedulerProvider)
                .subscribe(
                    { result ->
                        isEndPage = result.isEnd ?: false
                        states.value = result.documentList?.run {
                            if(this.isEmpty()){
                                SearchResultIsEmptyState
                            }else {
                                AddSearchResultState(this)
                            }
                        } ?: SearchResultIsEmptyState
                    },
                    { error ->
                        errorState.value = ErrorState(error)
                    }
                )
        }
    }
}