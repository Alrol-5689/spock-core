package com.alejandro.spock.core.gtd.person.repository

import com.alejandro.spock.core.gtd.person.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PersonRepository : JpaRepository<Person, UUID>
