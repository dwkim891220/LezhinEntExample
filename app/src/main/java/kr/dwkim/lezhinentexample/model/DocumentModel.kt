package kr.dwkim.lezhinentexample.model

import kr.dwkim.lezhinentexample.repository.model.Document

class DocumentModel(document: Document) {
    val collection: String?
    val datetime: String?
    val display_sitename: String?
    val doc_url: String?
    val image_url: String?
    val thumbnail_url: String?
    val width: Int?
    val height: Int?

    init {
        this.collection = document.collection
        this.datetime = document.datetime
        this.display_sitename = document.display_sitename
        this.doc_url = document.doc_url
        this.image_url = document.image_url
        this.thumbnail_url = document.thumbnail_url
        this.width = document.width
        this.height = document.height
    }
}