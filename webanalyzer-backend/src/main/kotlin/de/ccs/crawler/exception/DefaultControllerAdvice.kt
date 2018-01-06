package de.ccs.crawler.exception

import de.ccs.crawler.analyzer.verifier.WebPageVerifier
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class DefaultControllerAdvice {

    private val log = LogFactory.getLog(DefaultControllerAdvice::class.java)

    @ExceptionHandler(Exception::class)
    fun errorHandler(request: HttpServletRequest, exception: Exception): ResponseEntity<ErrorInfo> {

        log.error("Request raised " + exception.printStackTrace().toString())

        return when (exception) {
            is IllegalArgumentException -> ResponseEntity(ErrorInfo(code = HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                    error = exception.message,
                    path = request.requestURI), HttpStatus.UNPROCESSABLE_ENTITY)

            is MethodArgumentNotValidException -> ResponseEntity(ErrorInfo(code = HttpStatus.BAD_REQUEST.toString(),
                    error = exception.message,
                    path = request.requestURI), HttpStatus.BAD_REQUEST)

            else -> ResponseEntity(ErrorInfo(code = HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    error = exception.message,
                    path = request.requestURI), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    data class ErrorInfo(val code: String, val error: String?, val path: String)
}