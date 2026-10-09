package dev.shiwa.onearunbackend.repository

import dev.shiwa.onearunbackend.model.session.RaceSession
import org.springframework.data.jpa.repository.JpaRepository

interface RaceSessionRepository : JpaRepository<RaceSession, String> {
    fun findByJoinCode(joinCode: String): RaceSession?
    fun existsByJoinCode(joinCode: String): Boolean
}