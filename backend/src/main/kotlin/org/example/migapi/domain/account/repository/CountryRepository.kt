package org.example.migapi.domain.account.repository

import org.example.migapi.domain.account.model.Country
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CacheConfig(cacheNames = ["country-id"])
interface CountryRepository : JpaRepository<Country, String> {

    @Cacheable("country-id", key = "#id", unless = "#result == null")
    override fun findById(id: String): Optional<Country>
}