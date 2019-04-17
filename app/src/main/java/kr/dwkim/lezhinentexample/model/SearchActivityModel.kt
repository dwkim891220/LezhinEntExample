package kr.dwkim.lezhinentexample.model

import kr.dwkim.lezhinentexample.repository.result.GetSearchImageResult

class SearchActivityModel(result: GetSearchImageResult) {
    val documentList: List<DocumentModel>?
    val isEnd: Boolean?

    init {
        this.documentList = result.documents.map { document ->
            DocumentModel(document)
        }

        this.isEnd = result.meta.is_end
    }
}