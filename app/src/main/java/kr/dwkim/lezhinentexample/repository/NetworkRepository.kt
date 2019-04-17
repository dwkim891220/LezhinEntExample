package kr.dwkim.lezhinentexample.repository

import io.reactivex.Single
import kr.dwkim.lezhinentexample.repository.result.GetSearchImageResult
import retrofit2.http.*

interface NetworkRepository {
    @GET(SEARCH_IMAGE)
    fun searchImage(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Single<GetSearchImageResult>
}