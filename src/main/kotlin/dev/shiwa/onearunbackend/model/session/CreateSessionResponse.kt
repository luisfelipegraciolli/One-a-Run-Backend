package dev.shiwa.onearunbackend.model.session

data class CreateSessionResponse(
    val sessionId: String,
    val joinCode: String,
    val runnerId: String
)
