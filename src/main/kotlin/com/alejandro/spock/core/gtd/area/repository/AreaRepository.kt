package com.alejandro.spock.core.gtd.area.repository

import com.alejandro.spock.core.gtd.area.model.Area
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AreaRepository : JpaRepository<Area, UUID>
