package isel.leic.daw.dawProject.model.inputModel

import isel.leic.daw.dawProject.model.entity.Checklist
import isel.leic.daw.dawProject.model.entity.TemplateItem

data class TemplateDTO(
        var templateName : String = "",
        var templateItems: MutableSet<TemplateItem>? = null,
        var checklists: MutableSet<Checklist>? = null,
        var templateId: Int = -1
)