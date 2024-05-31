package ua.kpi.its.lab.security.handler

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.ScientificArticleService
import ua.kpi.its.lab.security.svc.MagazineService
import ua.kpi.its.lab.security.entity.ScientificArticle
import ua.kpi.its.lab.security.entity.Magazine

@Component
class MagazineHandler(private val magazineService: MagazineService) {

    fun createMagazineHandler(request: ServerRequest): ServerResponse {
        val requestDto = request.body(MagazineRequestDto::class.java)
        val magazine = magazineService.create(
            Magazine(
                name = requestDto.name,
                topic = requestDto.topic,
                language = requestDto.language,
                establishDate = requestDto.establishDate,
                issn = requestDto.issn,
                price = requestDto.price,
                periodicity = requestDto.periodicity
            )
        )
        val responseDto = MagazineResponseDto(
            id = magazine.id,
            name = magazine.name,
            topic = magazine.topic,
            language = magazine.language,
            establishDate = magazine.establishDate,
            issn = magazine.issn,
            price = magazine.price,
            periodicity = magazine.periodicity
        )
        return ServerResponse.ok().body(responseDto)
    }

    fun getMagazineByIdHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val magazine = magazineService.getById(id)
        return if (magazine != null) {
            val responseDto = MagazineResponseDto(
                id = magazine.id,
                name = magazine.name,
                topic = magazine.topic,
                language = magazine.language,
                establishDate = magazine.establishDate,
                issn = magazine.issn,
                price = magazine.price,
                periodicity = magazine.periodicity
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.notFound().build()
        }
    }

    fun updateMagazineHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val requestDto = request.body(MagazineRequestDto::class.java)
        val magazine = magazineService.update(
            Magazine(
                id = id,
                name = requestDto.name,
                topic = requestDto.topic,
                language = requestDto.language,
                establishDate = requestDto.establishDate,
                issn = requestDto.issn,
                price = requestDto.price,
                periodicity = requestDto.periodicity
            )
        )
        val responseDto = MagazineResponseDto(
            id = magazine.id,
            name = magazine.name,
            topic = magazine.topic,
            language = magazine.language,
            establishDate = magazine.establishDate,
            issn = magazine.issn,
            price = magazine.price,
            periodicity = magazine.periodicity
        )
        return ServerResponse.ok().body(responseDto)
    }

    fun deleteMagazineHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        magazineService.deleteById(id)
        return ServerResponse.noContent().build()
    }

    fun getAllMagazinesHandler(request: ServerRequest): ServerResponse {
        val magazines = magazineService.getAll().map { magazine ->
            MagazineResponseDto(
                id = magazine.id,
                name = magazine.name,
                topic = magazine.topic,
                language = magazine.language,
                establishDate = magazine.establishDate,
                issn = magazine.issn,
                price = magazine.price,
                periodicity = magazine.periodicity
            )
        }
        return ServerResponse.ok().body(magazines)
    }
}

@Component
class ScientificArticleHandler(
    private val articleService: ScientificArticleService,
    private val magazineService: MagazineService
) {

    fun createArticleHandler(request: ServerRequest): ServerResponse {
        val requestDto = request.body(ScientificArticleRequestDto::class.java)
        val magazine1 = magazineService.getById(requestDto.magazine1Id)
        return if (magazine1 != null) {
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
            val responseDto = ScientificArticleResponseDto(
                id = article.id,
                title = article.title,
                author = article.author,
                writingDate = article.writingDate,
                countWords = article.countWords,
                links = article.links,
                origLang = article.origLang,
                magazine1Id = article.magazine1.id
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.badRequest().build()
        }
    }

    fun getArticleByIdHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val article = articleService.getById(id)
        return if (article != null) {
            val responseDto = ScientificArticleResponseDto(
                id = article.id,
                title = article.title,
                author = article.author,
                writingDate = article.writingDate,
                countWords = article.countWords,
                links = article.links,
                origLang = article.origLang,
                magazine1Id = article.magazine1.id
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.notFound().build()
        }
    }

    fun updateArticleHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val requestDto = request.body(ScientificArticleRequestDto::class.java)
        val magazine1 = magazineService.getById(requestDto.magazine1Id)
        return if (magazine1 != null) {
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
            val responseDto = ScientificArticleResponseDto(
                id = article.id,
                title = article.title,
                author = article.author,
                writingDate = article.writingDate,
                countWords = article.countWords,
                links = article.links,
                origLang = article.origLang,
                magazine1Id = article.magazine1.id
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.badRequest().build()
        }
    }

    fun deleteArticleHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        articleService.deleteById(id)
        return ServerResponse.noContent().build()
    }

    fun getAllArticlesHandler(request: ServerRequest): ServerResponse {
        val articles = articleService.getAll().map { article ->
            ScientificArticleResponseDto(
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
        return ServerResponse.ok().body(articles)
    }
}