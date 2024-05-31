package ua.kpi.its.lab.security.svc

import ua.kpi.its.lab.security.entity.Magazine

interface MagazineService {
    fun create(product: Magazine): Magazine
    fun getById(id: Long): Magazine?
    fun update(product: Magazine): Magazine
    fun deleteById(id: Long)
    fun getAll(): List<Magazine>
}