package com.alejandro.spock.core.gtd.event.repository

import com.alejandro.spock.core.gtd.event.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EventRepository : JpaRepository<Event, UUID>
