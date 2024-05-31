package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScientificArticleRequestDto(
    val title: String,
    val author: String,
    val writingDate: String,
    val countWords: String,
    val links: String,
    val origLang: Boolean,
    val magazine1Id: Long
)