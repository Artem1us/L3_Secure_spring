package ua.kpi.its.lab.security.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.security.entity.Magazine
import ua.kpi.its.lab.security.repo.MagazineRepository
import ua.kpi.its.lab.security.svc.MagazineService

@Service
class MagazineServiceImpl @Autowired constructor(
    private val magazineRepository: MagazineRepository
) : MagazineService {
    override fun create(magazine: Magazine): Magazine {
        return magazineRepository.save(magazine)
    }

    override fun getById(id: Long): Magazine? {
        return magazineRepository.findById(id).orElse(null)
    }

    override fun update(magazine: Magazine): Magazine {
        return magazineRepository.save(magazine)
    }

    override fun deleteById(id: Long) {
        magazineRepository.deleteById(id)
    }

    override fun getAll(): List<Magazine> {
        return magazineRepository.findAll()
    }
}