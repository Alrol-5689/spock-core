package com.alejandro.spock.core.gtd.tag.repository

import com.alejandro.spock.core.gtd.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TagRepository : JpaRepository<Tag, UUID> {
	fun findAllByOrderByNameAsc(): List<Tag>
	fun findByName(name: String): Tag?
}
