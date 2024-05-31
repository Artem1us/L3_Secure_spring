package ua.kpi.its.lab.security.svc

import ua.kpi.its.lab.security.entity.ScientificArticle

interface ScientificArticleService {
    fun create(module: ScientificArticle): ScientificArticle
    fun getById(id: Long): ScientificArticle?
    fun update(module: ScientificArticle): ScientificArticle
    fun deleteById(id: Long)
    fun getAll(): List<ScientificArticle>
}