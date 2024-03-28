package org.example.migapi.core.domain.repo

import org.example.migapi.core.domain.model.entity.Country
import org.springframework.data.jpa.repository.JpaRepository

interface CountryRepository : JpaRepository<Country, String> {
}