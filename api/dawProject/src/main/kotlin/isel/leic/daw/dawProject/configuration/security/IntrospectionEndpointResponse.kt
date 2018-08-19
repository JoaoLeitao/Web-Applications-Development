package isel.leic.daw.dawProject.configuration.security

data class IntrospectionEndpointResponse (
    val active : String = "",
    val sub : String = "",
    val scope : String = "",
    val client_id : String = "",
    val name : String = "",
    val user_id : String = "",
    val email : String = ""
)