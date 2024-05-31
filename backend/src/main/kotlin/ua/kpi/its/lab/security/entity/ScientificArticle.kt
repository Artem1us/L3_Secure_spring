package ua.kpi.its.lab.security.entity

import jakarta.persistence.*

@Entity
data class ScientificArticle(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,
    val author: String,
    val writingDate: String,
    val countWords: String,
    val links: String,
    val origLang: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magazine_id")
    val magazine1: Magazine
)