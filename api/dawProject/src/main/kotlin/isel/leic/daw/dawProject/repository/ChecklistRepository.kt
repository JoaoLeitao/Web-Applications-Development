package isel.leic.daw.dawProject.repository

import isel.leic.daw.dawProject.model.entity.Account
import isel.leic.daw.dawProject.model.entity.Checklist
import isel.leic.daw.dawProject.model.entity.Template
import org.springframework.data.jpa.repository.JpaRepository

interface ChecklistRepository : JpaRepository<Checklist, Int> {
    fun findByTemplate(template: Template) : List<Checklist>
    fun findByAccount(account: Account): List<Checklist>
}