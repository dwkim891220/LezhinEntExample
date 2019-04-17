package kr.dwkim.lezhinentexample.model

import android.content.Context
import io.reactivex.Single
import kr.dwkim.lezhinentexample.repository.IRepository
import kr.dwkim.lezhinentexample.repository.NetworkRepository
import kr.dwkim.lezhinentexample.repository.result.GetSearchImageResult

class RepositoryImpl(
    private val context: Context,
    private val api: NetworkRepository
): IRepository {
    override fun searchImage(keyword: String, page: Int): Single<SearchActivityModel> =
        api.searchImage(keyword, page).map { result -> SearchActivityModel(result) }
}