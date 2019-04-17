package kr.dwkim.lezhinentexample.repository.model

data class Document(
    val collection: String?,
    val datetime: String?,
    val display_sitename: String?,
    val doc_url: String?,
    val image_url: String?,
    val thumbnail_url: String?,
    val width: Int?,
    val height: Int?
)