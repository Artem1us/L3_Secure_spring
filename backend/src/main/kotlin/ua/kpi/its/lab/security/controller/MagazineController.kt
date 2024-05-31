package ua.kpi.its.lab.rest.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.MagazineService
import ua.kpi.its.lab.security.entity.Magazine

@RestController
@RequestMapping("/api/Magazines")
class MagazineController(private val magazine1: MagazineService) {

    @PostMapping
    fun createMagazine(@RequestBody requestDto: MagazineRequestDtos): ResponseEntity<MagazineResponseDtos> {
        val magazine = magazine1.create(
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
        val responseDto = MagazineResponseDtos(
            id = magazine.id,
            name = magazine.name,
            topic = magazine.topic,
            language = magazine.language,
            establishDate = magazine.establishDate,
            issn = magazine.issn,
            price = magazine.price,
            periodicity = magazine.periodicity
        )
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/{id}")
    fun getMagazineById(@PathVariable id: Long): ResponseEntity<MagazineResponseDtos> {
        val magazine = magazine1.getById(id)
        return if (magazine != null) {
            val responseDto = MagazineResponseDtos(
                id = magazine.id,
                name = magazine.name,
                topic = magazine.topic,
                language = magazine.language,
                establishDate = magazine.establishDate,
                issn = magazine.issn,
                price = magazine.price,
                periodicity = magazine.periodicity
            )
            ResponseEntity.ok(responseDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateMagazine(@PathVariable id: Long, @RequestBody requestDto: MagazineRequestDtos): ResponseEntity<MagazineResponseDtos> {
        val magazine = magazine1.update(
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
        val responseDto = MagazineResponseDtos(
            id = magazine.id,
            name = magazine.name,
            topic = magazine.topic,
            language = magazine.language,
            establishDate = magazine.establishDate,
            issn = magazine.issn,
            price = magazine.price,
            periodicity = magazine.periodicity
        )
        return ResponseEntity.ok(responseDto)
    }

    @DeleteMapping("/{id}")
    fun deleteMagazine(@PathVariable id: Long): ResponseEntity<Void> {
        magazine1.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllMagazines(): ResponseEntity<List<MagazineResponseDtos>> {
        val magazines = magazine1.getAll().map { magazine ->
            MagazineResponseDtos(
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
        return ResponseEntity.ok(magazines)
    }
}