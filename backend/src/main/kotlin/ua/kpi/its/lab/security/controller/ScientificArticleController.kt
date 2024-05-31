package ua.kpi.its.lab.rest.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.ScientificArticleRequestDto
import ua.kpi.its.lab.security.dto.ScientificArticleResponseDtos
import ua.kpi.its.lab.security.svc.ScientificArticleService
import ua.kpi.its.lab.security.svc.MagazineService
import ua.kpi.its.lab.security.entity.ScientificArticle

@RestController
@RequestMapping("/api/articles")
class ScientificArticleController(
    private val articleService: ScientificArticleService,
    private val magazine1: MagazineService
) {

    @PostMapping
    fun createArticle(@RequestBody requestDto: ScientificArticleRequestDto): ResponseEntity<ScientificArticleResponseDtos> {
        val magazine1 = magazine1.getById(requestDto.magazine1Id)
        if (magazine1 != null) {
            val article = articleService.create(
                ScientificArticle(
                    title = requestDto.title,
                    author = requestDto.author,
                    writingDate = requestDto.writingDate,
                    countWords = requestDto.countWords,
                    links = requestDto.links,
                    origLang = requestDto.origLang,
                    magazine1 = magazine1
                )
            )
            val responseDto = ScientificArticleResponseDtos(
                id = article.id,
                title = article.title,
                author = article.author,
                writingDate = article.writingDate,
                countWords = article.countWords,
                links = article.links,
                origLang = article.origLang,
                magazine1Id = article.magazine1.id
            )
            return ResponseEntity.ok(responseDto)
        } else {
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    fun getArticleById(@PathVariable id: Long): ResponseEntity<ScientificArticleResponseDtos> {
        val article = articleService.getById(id)
        return if (article != null) {
            val responseDto = ScientificArticleResponseDtos(
                id = article.id,
                title = article.title,
                author = article.author,
                writingDate = article.writingDate,
                countWords = article.countWords,
                links = article.links,
                origLang = article.origLang,
                magazine1Id = article.magazine1.id
            )
            ResponseEntity.ok(responseDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateArticle(@PathVariable id: Long, @RequestBody requestDto: ScientificArticleRequestDto): ResponseEntity<ScientificArticleResponseDtos> {
        val magazine1 = magazine1.getById(requestDto.magazine1Id)
        if (magazine1 != null) {
            val article = articleService.update(
                ScientificArticle(
                    id = id,
                    title = requestDto.title,
                    author = requestDto.author,
                    writingDate = requestDto.writingDate,
                    countWords = requestDto.countWords,
                    links = requestDto.links,
                    origLang = requestDto.origLang,
                    magazine1 = magazine1
                )
            )
            val responseDto = ScientificArticleResponseDtos(
                id = article.id,
                title = article.title,
                author = article.author,
                writingDate = article.writingDate,
                countWords = article.countWords,
                links = article.links,
                origLang = article.origLang,
                magazine1Id = article.magazine1.id
            )
            return ResponseEntity.ok(responseDto)
        } else {
            return ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteArticle(@PathVariable id: Long): ResponseEntity<Void> {
        articleService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllArticles(): ResponseEntity<List<ScientificArticleResponseDtos>> {
        val articles = articleService.getAll().map { article ->
            ScientificArticleResponseDtos(
                id = article.id,
                title = article.title,
                author = article.author,
                writingDate = article.writingDate,
                countWords = article.countWords,
                links = article.links,
                origLang = article.origLang,
                magazine1Id = article.magazine1.id
            )
        }
        return ResponseEntity.ok(articles)
    }
}