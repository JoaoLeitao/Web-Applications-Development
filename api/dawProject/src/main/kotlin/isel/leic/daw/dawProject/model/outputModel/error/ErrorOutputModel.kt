package isel.leic.daw.dawProject.model.outputModel.error

import java.time.LocalDateTime

data class ErrorOutputModel(
        val timestamp: LocalDateTime = LocalDateTime.now(),
        val status: Int,
        val error: String,
        val message: String,
        val path: String
)