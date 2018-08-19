package isel.leic.daw.dawProject.configuration.security

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.leic.daw.dawProject.model.outputModel.error.ErrorOutputModel
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OpenIdConnectFilter: Filter {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val hreq = request as HttpServletRequest
        val resp = response as HttpServletResponse

        val swaggerResources = hreq.requestURI == "/api/v2/api-docs"
        val swaggerUI = hreq.requestURI.contains("swagger")
        val api = hreq.requestURI.toString().contains("/api")

        if(hreq.method == "OPTIONS" || swaggerResources || swaggerUI || !api)
            chain.doFilter(request, response)

        else {
            val authHeader = hreq.getHeader("Authorization")
            val checkToken = authHeader.startsWith("Bearer")
            if(!checkToken) {

                resp.status = HttpStatus.BAD_REQUEST.value()
                resp.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8.toString()

                val headers = HttpHeaders()
                headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

                val error = ResponseEntity(
                        ErrorOutputModel(
                                status = HttpStatus.BAD_REQUEST.value(),
                                error = "Bad Request",
                                message = "Bad Request - input maybe not correct!",
                                path = hreq.requestURI
                        ), headers, HttpStatus.BAD_REQUEST
                )
                resp.writer.write(jacksonObjectMapper().writeValueAsString(error))
            }
            val token = authHeader.split(" ")[1]

            val ier = introspectionEndpointRequest(token)

            if(ier.active == "true") {
                request.setAttribute("sub", ier.sub)
                request.setAttribute("username", ier.user_id)

                chain.doFilter(request, response)
            }
            else {
                resp.status = HttpStatus.UNAUTHORIZED.value()
                resp.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8.toString()

                val headers = HttpHeaders()
                headers.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8

                val error = ResponseEntity(
                        ErrorOutputModel(
                                status = HttpStatus.UNAUTHORIZED.value(),
                                error = "Unauthorized",
                                message = "Not Authorized to access this resource!",
                                path = hreq.requestURI
                        ), headers, HttpStatus.BAD_REQUEST
                )
                resp.writer.write(jacksonObjectMapper().writeValueAsString(error))
            }
        }
    }

    private fun introspectionEndpointRequest(token: String) : IntrospectionEndpointResponse {

        val introspectionEndpoint = "http://35.197.198.121/openid-connect-server-webapp/introspect"
        val clientId = "daw"
        val secretId = "secret"

        val arrayToken = "$clientId:$secretId".toByteArray(Charsets.UTF_8)
        val stringToken = Base64.getEncoder().encodeToString(arrayToken)

        val url = URL(introspectionEndpoint)
        val connection : HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", "Basic $stringToken")
        connection.doOutput = true

        val writer = connection.outputStream.writer(Charsets.UTF_8)
        writer.write("token=$token")
        writer.flush()
        writer.close()

        connection.connect()

        val responseString = getResponseString(connection)

        return getIntrospectionEndpointResponseObject(responseString)
    }

    private fun getResponseString(connection: HttpURLConnection): String {
        val stringBuilder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(connection.inputStream))

        var aux = reader.readLine()
        while (aux != null){
            stringBuilder.append(aux)
            aux = reader.readLine()
        }
        return stringBuilder.toString()
    }

    private fun getIntrospectionEndpointResponseObject(string: String) : IntrospectionEndpointResponse {
        val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.readValue(string, IntrospectionEndpointResponse::class.java)
    }

    override fun init(filterConfig: FilterConfig?) {

    }

    override fun destroy() {

    }
}