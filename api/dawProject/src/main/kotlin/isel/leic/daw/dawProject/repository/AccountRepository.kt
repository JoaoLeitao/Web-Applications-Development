package isel.leic.daw.dawProject.repository

import isel.leic.daw.dawProject.model.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, String> {
    fun findBySub(sub: String) : Optional<Account>
}