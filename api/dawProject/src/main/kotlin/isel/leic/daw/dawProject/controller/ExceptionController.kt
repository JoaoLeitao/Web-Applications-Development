package isel.leic.daw.dawProject.controller

import isel.leic.daw.dawProject.configuration.*
import isel.leic.daw.dawProject.model.outputModel.error.ErrorOutputModel
import org.hibernate.HibernateException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
@RestController
@RequestMapping(produces = ["application/problem+json"])
class ExceptionController {

    @ExceptionHandler(value = [HibernateException::class])
    fun hibernateError(req: HttpServletRequest, e: Exception) : ResponseEntity<ErrorOutputModel> {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

        return ResponseEntity(
                ErrorOutputModel(
                        status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        error = "Internal Server Error",
                        message = "The server encountered an unexpected condition that prevented it from fulfilling the request",
                        path = req.requestURI
                ), headers, HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun notFoundError(req: HttpServletRequest, e: Exception) : ResponseEntity<ErrorOutputModel> {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

        return ResponseEntity(
                ErrorOutputModel(
                        status = HttpStatus.NOT_FOUND.value(),
                        error = "Not Found",
                        message = "The resource was not found",
                        path = req.requestURI
                ), headers, HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(value = [ForbiddenException::class])
    fun forbiddenError(req: HttpServletRequest, e: Exception) : ResponseEntity<ErrorOutputModel> {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

        return ResponseEntity(
                ErrorOutputModel(
                        status = HttpStatus.FORBIDDEN.value(),
                        error = "Forbidden",
                        message = e.message!!,
                        path = req.requestURI
                ), headers, HttpStatus.FORBIDDEN
        )
    }

    @ExceptionHandler(value = [BadRequestException::class])
    fun badRequestError(req: HttpServletRequest, e: Exception) : ResponseEntity<ErrorOutputModel> {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

        return ResponseEntity(
                ErrorOutputModel(
                        status = HttpStatus.BAD_REQUEST.value(),
                        error = "Bad Request",
                        message = e.message!!,
                        path = req.requestURI
                ), headers, HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(value = [NotAuthorizedException::class])
    fun notAuthorizedError(req: HttpServletRequest, e: Exception) : ResponseEntity<ErrorOutputModel> {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

        return ResponseEntity(
                ErrorOutputModel(
                        status = HttpStatus.UNAUTHORIZED.value(),
                        error = "Unauthorized",
                        message = e.message!!,
                        path = req.requestURI
                ), headers, HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(value = [ConflictException::class])
    fun conflictError(req: HttpServletRequest, e: Exception) : ResponseEntity<ErrorOutputModel> {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

        return ResponseEntity(
                ErrorOutputModel(
                        status = HttpStatus.CONFLICT.value(),
                        error = "Conflict",
                        message = e.message!!,
                        path = req.requestURI
                ), headers, HttpStatus.CONFLICT
        )
    }
}