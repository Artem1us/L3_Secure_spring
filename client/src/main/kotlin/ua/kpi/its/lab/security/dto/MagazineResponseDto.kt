package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable
data class MagazineResponseDto(
    val id: Long,
    val name: String,
    val topic: String,
    val language: String,
    val establishDate: String,
    val issn: String,
    val price: String,
    val periodicity: Boolean
)