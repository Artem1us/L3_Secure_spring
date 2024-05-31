package ua.kpi.its.lab.security.dto

data class ScientificArticleResponseDto(
    val id: Long,
    val title: String,
    val author: String,
    val writingDate: String,
    val countWords: String,
    val links: String,
    val origLang: Boolean,
    val magazine1Id: Long
)