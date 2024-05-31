package ua.kpi.its.lab.security.dto

data class MagazineRequestDtos(
    val name: String,
    val topic: String,
    val language: String,
    val establishDate: String,
    val issn: String,
    val price: String,
    val periodicity: Boolean
)

data class MagazineResponseDtos(
    val id: Long,
    val name: String,
    val topic: String,
    val language: String,
    val establishDate: String,
    val issn: String,
    val price: String,
    val periodicity: Boolean
)

data class ScientificArticleRequestDtos(
    val title: String,
    val author: String,
    val writingDate: String,
    val countWords: String,
    val links: String,
    val origLang: Boolean,
    val magazine1Id: Long
)

data class ScientificArticleResponseDtos(
    val id: Long,
    val title: String,
    val author: String,
    val writingDate: String,
    val countWords: String,
    val links: String,
    val origLang: Boolean,
    val magazine1Id: Long
)