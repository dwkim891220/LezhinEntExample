package kr.dwkim.lezhinentexample.repository

import io.reactivex.Single
import kr.dwkim.lezhinentexample.model.SearchActivityModel

interface IRepository {
    fun searchImage(keyword: String, page: Int): Single<SearchActivityModel>
}