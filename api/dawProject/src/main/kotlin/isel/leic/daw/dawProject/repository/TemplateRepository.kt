package isel.leic.daw.dawProject.repository

import isel.leic.daw.dawProject.model.entity.Account
import isel.leic.daw.dawProject.model.entity.Template
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TemplateRepository : JpaRepository<Template, Int>{
    fun findByTemplateIdAndAccount(id: Int, account: Account) : Optional<Template>
    fun findByAccount(account: Account): List<Template>
}