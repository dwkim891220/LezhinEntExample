package kr.dwkim.lezhinentexample.repository.result

import com.google.gson.annotations.SerializedName
import kr.dwkim.lezhinentexample.repository.model.Document
import kr.dwkim.lezhinentexample.repository.model.Meta

data class GetSearchImageResult(
    @SerializedName("documents") val documents: List<Document>,
    @SerializedName("meta") val meta: Meta
)