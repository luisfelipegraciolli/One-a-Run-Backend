package dev.shiwa.onearunbackend.model.session

data class JoinSessionResponse(
    val sessionId: String,
    val guestRunnerId: String,
    val distanceMeters: Int
)