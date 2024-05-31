package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable
data class MagazineRequestDto(
    val name: String,
    val topic: String,
    val language: String,
    val establishDate: String,
    val issn: String,
    val price: String,
    val periodicity: Boolean
)