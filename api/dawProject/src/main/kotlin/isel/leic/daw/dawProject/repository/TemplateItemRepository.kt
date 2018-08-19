package isel.leic.daw.dawProject.repository

import isel.leic.daw.dawProject.model.entity.Account
import isel.leic.daw.dawProject.model.entity.Template
import isel.leic.daw.dawProject.model.entity.TemplateItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TemplateItemRepository : JpaRepository<TemplateItem, Int> {
    fun findByTemplateItemIdAndTemplate(templateItemId: Int, template: Template): Optional<TemplateItem>
}